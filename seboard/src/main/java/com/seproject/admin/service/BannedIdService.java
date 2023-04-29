package com.seproject.admin.service;

import com.seproject.admin.domain.BannedId;
import com.seproject.admin.repository.BannedIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.seproject.admin.dto.BannedIdDTO.*;

@RequiredArgsConstructor
@Service
public class BannedIdService {

    private final BannedIdRepository bannedIdRepository;

    public RetrieveAllBannedIdResponse findAll() {
        List<BannedId> all = bannedIdRepository.findAll();
        return RetrieveAllBannedIdResponse.toDTO(all);
    }

    public boolean possibleId(String id) {
        return !bannedIdRepository.existsByBannedId(id);
    }

    public void banId(String id) {
        if(!bannedIdRepository.existsByBannedId(id)) {
            BannedId bannedId = BannedId.builder()
                    .bannedId(id)
                    .build();

            bannedIdRepository.save(bannedId);
        }
    }

    public void unbanId(String id) {
        BannedId bannedId = bannedIdRepository.findByBannedId(id).orElseThrow();
        bannedIdRepository.delete(bannedId);
    }
}
