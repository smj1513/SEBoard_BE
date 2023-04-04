package com.seproject.seboard.application.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class AppServiceHelper {
    public static <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
    }
}
