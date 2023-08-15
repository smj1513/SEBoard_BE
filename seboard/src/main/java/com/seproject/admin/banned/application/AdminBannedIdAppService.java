package com.seproject.admin.banned.application;

import com.seproject.admin.banned.controller.dto.BannedIdDTO.BannedIdResponse;
import com.seproject.admin.banned.domain.BannedId;
import com.seproject.admin.banned.persistence.BannedIdQueryRepository;
import com.seproject.admin.banned.service.BannedIdService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminBannedIdAppService {

    private final BannedIdService bannedIdService;
    private final BannedIdQueryRepository bannedIdQueryRepository;

    public Page<BannedIdResponse> findAll(int page, int perPage) {
        PageRequest pageRequest = PageRequest.of(page, perPage);
        return bannedIdQueryRepository.findAll(pageRequest);
    }

    @Transactional
    public Long create(String bannedId) {
        Optional<BannedId> byId = bannedIdService.findById(bannedId);

        if(byId.isPresent()) {
            throw new CustomIllegalArgumentException(ErrorCode.ALREADY_EXIST_BANNED_ID,null);
        }

        return bannedIdService.createBannedId(bannedId);
    }

    @Transactional
    public void delete(String bannedId) {
        bannedIdService.deleteBannedId(bannedId);
    }
}
