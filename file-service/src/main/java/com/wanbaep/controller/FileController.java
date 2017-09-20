package com.wanbaep.controller;

import com.wanbaep.domain.FileDomain;
import com.wanbaep.service.FileService;
import com.wanbaep.utils.ErrorResponse;
import com.wanbaep.utils.FileUtils;
import com.wanbaep.utils.JdbcEmptyResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/")
public class FileController {

    private FileService fileService;
    private FileUtils fileUtils;

    @Autowired
    public FileController(FileService fileService, FileUtils fileUtils) {
        this.fileService = fileService;
        this.fileUtils = fileUtils;
    }

    @GetMapping
    public ModelAndView mainPage() {
        return new ModelAndView("fileview");
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileDomain>> getFiles() {
        List<FileDomain> fileDomains = fileService.findAll();
        return new ResponseEntity<>(fileDomains, HttpStatus.OK);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<InputStreamResource> getFileById(@PathVariable("id") Long id) {
        FileDomain fileDomain = fileService.findFileById(id);
        String originalFileName = "";

        try {
            originalFileName = URLEncoder.encode(fileDomain.getFileName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\";");
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, fileDomain.getContentType());
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, fileDomain.getFileLength().toString());

        File file = new File(fileDomain.getSaveFileName());
        InputStreamResource inputStreamResource = null;
        try {
            inputStreamResource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/files")
    public ResponseEntity<FileDomain> saveFile(@RequestParam("file") MultipartFile multipartFile,
                                               @RequestParam(value = "update", required = false) String updateFlag) {

        FileDomain fileDomain = fileService.uploadFile(multipartFile);

        if(updateFlag == null) {
            fileDomain = fileService.saveFile(fileDomain);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_LOCATION,"/files/" + fileDomain.getId());
        return new ResponseEntity<>(fileDomain,httpHeaders,HttpStatus.CREATED);
    }

    @PutMapping("/files/{id}")
    public ResponseEntity<FileDomain> updateFileById(@PathVariable("id") Long id, @RequestBody FileDomain fileDomain) {
        FileDomain oldFile = fileService.findFileById(id);
        fileUtils.removeFile(oldFile.getSaveFileName());

        fileDomain.setId(id);
        FileDomain updatedFile = fileService.updateFileById(fileDomain);

        return new ResponseEntity<>(updatedFile,HttpStatus.OK);
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<Long> deleteFileById(@PathVariable("id") Long id) {
        FileDomain fileDomain = fileService.findFileById(id);
        fileUtils.removeFile(fileDomain.getSaveFileName());
        Long fileId = fileService.deleteFileById(id);
        return new ResponseEntity<>(fileId, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(JdbcEmptyResultException.class)
    public ResponseEntity<ErrorResponse> handleInvalidException(JdbcEmptyResultException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + exception.getId() + "]에 해당하는 계정이 없습니다.");
        errorResponse.setCode("file.not.found.exception");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
