package com.seproject.admin.service;

import com.seproject.admin.domain.BannedNickname;
import com.seproject.admin.domain.repository.BannedNicknameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.seproject.admin.dto.BannedNicknameDTO.*;
@RequiredArgsConstructor
@Service
public class BannedNicknameService {
    private final BannedNicknameRepository bannedNicknameRepository;


    public RetrieveAllBannedNicknameResponse findAll() {
        return RetrieveAllBannedNicknameResponse.toDTO(bannedNicknameRepository.findAll());
    }
    public boolean possibleNickname(String nickname) {
        return !bannedNicknameRepository.existsByBannedNickname(nickname);
    }

    public void unbanNickname(String nickname) {
        BannedNickname bannedNickname = bannedNicknameRepository.findByBannedNickname(nickname).orElseThrow();
        bannedNicknameRepository.delete(bannedNickname);
    }

    public void banNickname(String nickname) {
        if(!bannedNicknameRepository.existsByBannedNickname(nickname)) {
            BannedNickname bannedNickname = BannedNickname.builder()
                    .bannedNickname(nickname)
                    .build();

            bannedNicknameRepository.save(bannedNickname);
        }
    }
}
