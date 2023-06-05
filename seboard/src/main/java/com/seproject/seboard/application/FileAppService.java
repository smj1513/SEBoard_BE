package com.seproject.seboard.application;

import com.seproject.admin.domain.FileConfiguration;
import com.seproject.admin.domain.repository.FileConfigurationRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.ExceedFileSizeException;
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
    private final FileConfigurationRepository fileConfigurationRepository;
    private final Long MEGA_BYTE_TO_BYTE = 1024L * 1024L;

    public FileMetaDataListResponse uploadFiles(List<MultipartFile> fileList) {
        List<FileMetaData> fileMetaDataList = new ArrayList<>();
        Long maxFileSize = fileConfigurationRepository.findAll().stream().findFirst().orElseGet(
                () -> new FileConfiguration(100L, 100L)
        ).getMaxSizePerFile();

        fileList.forEach(multipartFile -> {
            if(maxFileSize < multipartFile.getSize()/MEGA_BYTE_TO_BYTE){
                throw new ExceedFileSizeException(ErrorCode.INVALID_FILE_SIZE);
            }

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
