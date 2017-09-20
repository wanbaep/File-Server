package com.wanbaep.service;

import com.wanbaep.domain.FileDomain;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    FileDomain findFileById(Long id);
    List<FileDomain> findAll();
    Long deleteFileById(Long id);
    FileDomain updateFileById(FileDomain fileDomain);
    FileDomain saveFile(FileDomain fileDomain);
    FileDomain uploadFile(MultipartFile multipartFile);
}
