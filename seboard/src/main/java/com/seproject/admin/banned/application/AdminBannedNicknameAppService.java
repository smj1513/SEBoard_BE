package com.seproject.admin.banned.application;

import com.seproject.admin.banned.controller.dto.BannedNicknameDTO.BannedNicknameResponse;
import com.seproject.admin.banned.domain.BannedNickname;
import com.seproject.admin.banned.persistence.BannedNicknameQueryRepository;
import com.seproject.admin.banned.service.BannedNicknameService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AdminBannedNicknameAppService {
    private final BannedNicknameService bannedNicknameService;

    private final BannedNicknameQueryRepository bannedNicknameQueryRepository;
    public Page<BannedNicknameResponse> findAll(int page,int perPage) {
        PageRequest pageRequest = PageRequest.of(page, perPage);
        return bannedNicknameQueryRepository.findAll(pageRequest);
    }

    @Transactional
    public Long createBannedNickname(String nickname) {
        Optional<BannedNickname> byNickname = bannedNicknameService.findByNickname(nickname);
        if (byNickname.isPresent()) {
            throw new CustomIllegalArgumentException(ErrorCode.ALREADY_EXIST_BANNED_NICKNAME,null);
        }

        return bannedNicknameService.createBannedNickname(nickname);
    }

    @Transactional
    public void deleteBannedNickname(String nickname) {
        bannedNicknameService.deleteBannedNickname(nickname);
    }
}
