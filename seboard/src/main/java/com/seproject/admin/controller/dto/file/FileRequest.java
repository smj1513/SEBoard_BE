package com.seproject.admin.controller.dto.file;

import lombok.Data;

import java.util.List;

public class FileRequest {
    @Data
    public static class BulkFileRequest{
        private List<Long> fileIds;
    }
}
