package com.seproject.board.bulletin.controller;

import com.seproject.board.common.BaseTime;
import com.seproject.file.domain.model.FileMetaData;
import com.seproject.global.BannerSetup;
import com.seproject.global.FileMetaDataSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BannerControllerTest {

    @Autowired
    BannerSetup bannerSetup;

    @Autowired
    FileMetaDataSetup fileMetaDataSetup;

    @Autowired
    EntityManager em;

    @Autowired
    MockMvc mvc;

    static final String url = "/banners/";


    @Test
    public void 배너_조회() throws Exception {
        for (int i = 0; i < 3; i++) {
            FileMetaData img = fileMetaDataSetup.createImageFileMetaData(new BaseTime(), "jpg");
            bannerSetup.createActiveBanner(img);
        }

        em.flush(); em.clear();

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));

        for (int i = 0; i < 3; i++) {
            perform
                    .andExpect(jsonPath("$[" + i + "].fileMetaData").exists())
                    .andExpect(jsonPath("$[" + i + "].bannerUrl").exists());
        }
    }


}