package com.seproject;

import com.seproject.account.account.domain.FormAccount;
import com.seproject.board.common.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    public void init() throws Exception {
//        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {

        private final EntityManager em;

        public void init() {

            FormAccount userAccount = FormAccount.builder()
                    .accountId(100000L)
                    .loginId("user")
                    .name("user_name")
                    .password("$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6")
                    .createdAt( LocalDateTime.of(2023, 04, 26, 12, 12, 10))
                    .status(Status.NORMAL)
                    .build();

            em.persist(userAccount);
        }
    }
}
