package com.seproject.seboard.domain.repository.commons;

import com.seproject.seboard.domain.model.common.FileMetaData;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {
    FileMetaData save(MultipartFile file);
    void delete(String fileName);
}
