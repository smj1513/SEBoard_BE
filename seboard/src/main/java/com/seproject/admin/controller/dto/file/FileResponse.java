package com.seproject.admin.controller.dto.file;

import com.seproject.admin.domain.FileExtension;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class FileResponse {
    @Data
    public static class FileExtensionResponse {
        private List<String> extensionName;

        protected FileExtensionResponse(List<String> extensionName) {
            this.extensionName = extensionName;
        }

        public static FileExtensionResponse of(List<FileExtension> fileExtensions){
            List<String> extenionList = fileExtensions.stream().map(FileExtension::getExtensionName).collect(Collectors.toList());
            return new FileExtensionResponse(extenionList);
        }
    }
}
