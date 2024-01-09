package com.seproject.admin.banned.service;

import com.seproject.admin.banned.controller.dto.SpamWordRequest;
import com.seproject.admin.banned.controller.dto.SpamWordRequest.SpamWordCreateRequest;
import com.seproject.admin.banned.controller.dto.SpamWordResponse;
import com.seproject.admin.banned.domain.SpamWord;
import com.seproject.admin.banned.domain.repository.SpamWordRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Trantactional
public class SpamWordService {
    private final SpamWordRepository spamWordRepository;

    public List<SpamWordResponse> findAll() {
        //TODO : 개수 제한?
        return spamWordRepository.findAllResponse();
    }


    public void createSpamWord(SpamWordCreateRequest request) {
        if(spamWordRepository.existsByWord(request.getWord())){
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_REQUEST, null);
        }

        if(!request.getWord().isBlank()){
            SpamWord spamWord = SpamWord.of(request.getWord());
            spamWordRepository.save(spamWord);
        }
    }

    public void deleteSpamWord(Long id){
        if(spamWordRepository.findById(id).isEmpty()){
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_REQUEST, null);
        }

        spamWordRepository.deleteById(id);
    }
}
