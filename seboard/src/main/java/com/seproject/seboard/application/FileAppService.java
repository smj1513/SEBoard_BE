package com.seproject.seboard.application;

import com.seproject.seboard.application.utils.FileUtils;
import com.seproject.seboard.controller.dto.FileMetaDataResponse.FileMetaDataListResponse;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.repository.commons.FileMetaDataRepository;
import com.seproject.seboard.domain.repository.commons.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileAppService {
    private final FileMetaDataRepository fileMetaDataRepository;
    private final FileRepository fileRepository;

    public FileMetaDataListResponse uploadFiles(List<MultipartFile> fileList) {
        List<FileMetaData> fileMetaDataList = new ArrayList<>();

        fileList.forEach(multipartFile -> {
            FileMetaData fileMetaData = fileRepository.save(multipartFile);
            fileMetaDataRepository.save(fileMetaData);
            fileMetaDataList.add(fileMetaData);
        });

        return new FileMetaDataListResponse(fileMetaDataList);
    }

    public void deleteFile(Long fileMetaDataId) {
        FileMetaData fileMetaData = fileMetaDataRepository.findById(fileMetaDataId).orElseThrow(IllegalArgumentException::new);

        fileRepository.delete(fileMetaData.getFilePath());
        fileMetaDataRepository.delete(fileMetaData);
    }

}
