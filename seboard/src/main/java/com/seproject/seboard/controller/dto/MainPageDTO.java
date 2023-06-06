package com.seproject.seboard.controller.dto;

import com.seproject.seboard.domain.model.category.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.seproject.seboard.controller.dto.post.PostResponse.*;

public class MainPageDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveMainPageResponse {
        private Page<RetrievePostListResponseElement> posts;
        private String menuName;
        private String urlId;

        public static RetrieveMainPageResponse toDTO(Page<RetrievePostListResponseElement> posts, Menu menu) {
            return builder()
                    .posts(posts)
                    .urlId(menu.getUrlInfo())
                    .menuName(menu.getName())
                    .build();
        }
    }
}
