package com.seproject.admin.domain.repository;

import com.seproject.admin.dto.AccountDTO;
import com.seproject.admin.dto.AccountDTO.AdminRetrieveAccountCondition;
import com.seproject.admin.dto.AccountDTO.RetrieveAccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminAccountSearchRepository {
    Page<RetrieveAccountResponse> findAllAccount(AdminRetrieveAccountCondition condition, Pageable pageable);
    Page<RetrieveAccountResponse> findDeletedAccount(Pageable pageable);

}
