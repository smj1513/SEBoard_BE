package com.seproject.seboard.application;

import com.seproject.seboard.application.utils.FileUtils;
import com.seproject.seboard.controller.dto.FileMetaDataResponse.FileMetaDataListResponse;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.repository.commons.FileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileAppService {
    private final FileMetaDataRepository fileMetaDataRepository;

    public FileMetaDataListResponse uploadFiles(List<MultipartFile> fileList) {
        List<FileMetaData> fileMetaDataList = new ArrayList<>();

        fileList.forEach(multipartFile -> {
            FileMetaData fileMetaData = saveFile(multipartFile, "/files");
            fileMetaDataList.add(fileMetaData);
        });

        return new FileMetaDataListResponse(fileMetaDataList);
    }

    private FileMetaData saveFile(MultipartFile file, String urlRootPath){
        FileMetaData fileMetaData = null;

        String originalFileName = file.getOriginalFilename();
        String storedFileName = FileUtils.getStoredFileName(originalFileName);
        String filePath = FileUtils.getFilePath()+"/"+storedFileName;
        String urlPath = FileUtils.getUrlPath(urlRootPath)+"/"+storedFileName;
        Long fileSize = file.getSize();

        try{
            File createdFile = new File(filePath);
            file.transferTo(createdFile);
            fileMetaData = new FileMetaData(originalFileName, storedFileName, filePath, urlPath, fileSize);
            fileMetaDataRepository.save(fileMetaData);
        }catch (Exception e){
            e.printStackTrace();
        }

        return fileMetaData;
    }

    public void deleteFile(Long fileMetaDataId) {
        FileMetaData fileMetaData = fileMetaDataRepository.findById(fileMetaDataId).orElseThrow(IllegalArgumentException::new);

        deleteFileFromStorage(fileMetaData);
        fileMetaDataRepository.delete(fileMetaData);
    }

    public void deleteFileFromStorage(FileMetaData fileMetaData){
        File existFile = new File(fileMetaData.getFilePath());

        if(existFile.exists()){
            existFile.delete();
        }
    }


}
