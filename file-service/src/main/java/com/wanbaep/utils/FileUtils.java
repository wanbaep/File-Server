package com.wanbaep.utils;

import com.wanbaep.domain.FileDomain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@PropertySource("classpath:/application.properties")
@Component(value = "fileUtils")
public class FileUtils {
    @Value("${application.file.basedir}")
    private String baseDir;

    public FileDomain uploadFileIO(MultipartFile multipartFile) {
        Date currentDate = new Date();
        String filePath = baseDir + new SimpleDateFormat("YYYY" + File.separator + "MM" + File.separator + "dd").format(currentDate);
        FileDomain fileDomain = mappingMultipartFileToFileDomain(multipartFile, currentDate, filePath);

        File file = new File(filePath);
        file.mkdirs();

        try {
            InputStream inputStream = multipartFile.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(fileDomain.getSaveFileName());

            int length;
            byte[] buffer = new byte[512];
            while((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer,0,length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileDomain;
    }

    public Boolean removeFile(String filePath) {
        File file = new File(filePath);
        Boolean result = file.delete();
        return result;
    }

    private FileDomain mappingMultipartFileToFileDomain(MultipartFile multipartFile, Date date, String filePath) {
        FileDomain fileDomain = new FileDomain();
        fileDomain.setFileName(multipartFile.getOriginalFilename());
        fileDomain.setSaveFileName(filePath + File.separator + UUID.randomUUID().toString());
        fileDomain.setFileLength(multipartFile.getSize());
        fileDomain.setContentType(multipartFile.getContentType());
        fileDomain.setCreateDate(date);
        fileDomain.setModifyDate(date);
        return fileDomain;
    }

}
