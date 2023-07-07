package com.seproject.file.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.file.controller.dto.FileRequest.AdminFileRetrieveCondition;
import com.seproject.file.controller.dto.FileResponse.AdminFileRetrieveResponse;
import com.seproject.file.controller.dto.FileResponse.FileConfigurationResponse;
import com.seproject.file.controller.dto.FileResponse.FileExtensionResponse;
import com.seproject.file.domain.model.FileConfiguration;
import com.seproject.file.domain.model.FileExtension;
import com.seproject.file.domain.repository.AdminFileMetaDataSearchRepository;
import com.seproject.file.domain.repository.FileConfigurationRepository;
import com.seproject.file.domain.repository.FileExtensionRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.board.common.utils.FileUtils;
import com.seproject.file.domain.repository.FileMetaDataRepository;
import com.seproject.file.domain.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminFileAppService {
    private final FileUtils fileUtils;
    private final FileMetaDataRepository fileMetaDataRepository;
    private final FileRepository fileRepository;
    private final FileConfigurationRepository fileConfigurationRepository;
    private final FileExtensionRepository fileExtensionRepository;
    private final AdminFileMetaDataSearchRepository fileMetaDataSearchRepository;

    public Page<AdminFileRetrieveResponse> retrieveFileMetaData(AdminFileRetrieveCondition condition, Pageable pageable) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        return fileMetaDataSearchRepository.findFileMetaDataByCondition(condition, pageable);
    }

    public void deleteBulkFile(List<Long> fileIds) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        fileMetaDataRepository.findAllById(fileIds).forEach(fileMetaData -> {
            fileRepository.delete(fileMetaData.getFilePath());
            fileMetaDataRepository.delete(fileMetaData);
        });
    }

    public void addFileExtension(List<String> extensions) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        //TODO : 중복 검사?
        extensions.forEach(extension -> {
            FileExtension fileExtension = new FileExtension(extension);
            fileUtils.addAllowedExtension(extension);
            fileExtensionRepository.save(fileExtension);
        });
    }

    public void removeFileExtension(List<String> extensions){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        //TODO : 중복 검사?
        extensions.forEach(extension -> {
            fileUtils.removeAllowedExtension(extension);
            fileExtensionRepository.removeByExtensionName(extension);
        });
    }

    public FileExtensionResponse retrieveFileExtension(){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        return FileExtensionResponse.of(fileExtensionRepository.findAll());
    }

    public void setFileConfiguration(Long maxSizePerFile, Long maxSizePerPost) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        FileConfiguration fileConfiguration = fileConfigurationRepository.findAll().stream().findFirst().orElseGet(() -> {
            return new FileConfiguration(100L, 100L);
        });

        fileConfiguration.setMaxSizePerFile(maxSizePerFile);
        fileConfiguration.setMaxSizePerPost(maxSizePerPost);

        fileConfigurationRepository.save(fileConfiguration);
    }

    public FileConfigurationResponse retrieveFileConfiguration() {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        FileConfiguration fileConfiguration = fileConfigurationRepository.findAll().stream().findFirst().orElseGet(() -> {
            return new FileConfiguration(100L, 100L);
        });

        return new FileConfigurationResponse(fileConfiguration);
    }
}
