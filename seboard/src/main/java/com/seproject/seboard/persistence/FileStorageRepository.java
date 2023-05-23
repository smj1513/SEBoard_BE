package com.seproject.seboard.persistence;

import com.seproject.seboard.application.utils.FileUtils;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.repository.commons.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Repository
@RequiredArgsConstructor
public class FileStorageRepository implements FileRepository {
    @Value("${storage.urlRootPath}")
    private String urlRootPath;
    private final FileUtils fileUtils;

    @Override
    public FileMetaData save(MultipartFile file) {
        FileMetaData fileMetaData = null;

        String originalFileName = file.getOriginalFilename();
        String storedFileName = fileUtils.getStoredFileName(originalFileName);
        String filePath = fileUtils.getFilePath()+"/"+storedFileName;
        String urlPath = fileUtils.getUrlPath(urlRootPath)+"/"+storedFileName;
        Long fileSize = file.getSize();

        try{
            File createdFile = new File(filePath);
            file.transferTo(createdFile);
            fileMetaData = new FileMetaData(originalFileName, storedFileName, filePath, urlPath, fileSize);
        }catch (Exception e){
            e.printStackTrace();
        }

        return fileMetaData;

    }

    @Override
    public void delete(String fileName) {
        File existFile = new File(fileName);

        if(existFile.exists()){
            existFile.delete();
        }
    }
}
