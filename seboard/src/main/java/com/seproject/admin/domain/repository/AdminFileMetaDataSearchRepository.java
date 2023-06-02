package com.seproject.admin.domain.repository;

import com.seproject.admin.controller.dto.file.FileRequest;
import com.seproject.admin.controller.dto.file.FileRequest.AdminFileRetrieveCondition;
import com.seproject.admin.controller.dto.file.FileResponse;
import com.seproject.admin.controller.dto.file.FileResponse.AdminFileRetrieveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminFileMetaDataSearchRepository {
    Page<AdminFileRetrieveResponse> findFileMetaDataByCondition(AdminFileRetrieveCondition condition, Pageable pageable);
}
