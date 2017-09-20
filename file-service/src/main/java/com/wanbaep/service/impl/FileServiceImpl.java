package com.wanbaep.service.impl;

import com.wanbaep.dao.FileDao;
import com.wanbaep.domain.FileDomain;
import com.wanbaep.service.FileService;
import com.wanbaep.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private FileDao fileDao;
    private FileUtils fileUtils;

    @Autowired
    public FileServiceImpl(FileDao fileDao, FileUtils fileUtils) {
        this.fileDao = fileDao;
        this.fileUtils = fileUtils;
    }

    @Override
    public FileDomain findFileById(Long id) {
        return fileDao.selectFileById(id);
    }

    @Override
    public List<FileDomain> findAll() {
        return fileDao.selectAll();
    }

    @Override
    public Long deleteFileById(Long id) {
        return fileDao.deleteFileById(id);
    }

    @Override
    public FileDomain updateFileById(FileDomain fileDomain) {
        fileDao.updateFileById(fileDomain);
        return fileDomain;

    }

    @Override
    public FileDomain saveFile(FileDomain fileDomain) {
        return fileDao.saveFile(fileDomain);
    }

    @Override
    public FileDomain uploadFile(MultipartFile multipartFile) {
        return fileUtils.uploadFileIO(multipartFile);
    }
}
