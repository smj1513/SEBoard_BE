package com.seproject.seboard.service;

import com.seproject.seboard.application.PostAppService;
import com.seproject.seboard.domain.model.Author;
import com.seproject.seboard.domain.model.Post;
import com.seproject.seboard.domain.service.PostService;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostAppServiceTest extends BaseAppServiceTest {

    private PostAppService postAppService;
    private PostService postService;
    @Override
    @BeforeAll
    public void init() {
        super.init();
        postService = mock(PostService.class);
        postAppService = new PostAppService(postService,postRepository,unnamedPostRepository,authorRepository,categoryRepository,commentRepository,bookmarkRepository);
    }

    @Test
    @DisplayName("게시글 삭제 성공 케이스")
    void deleteNamedPostTest() {
        Author author = createAuthor(1L,"alswhd1113","김민종");
        Post post = createMockPost(1L,author,null,"title","contents",false);
        Assertions.assertDoesNotThrow(() -> postAppService.deleteNamedPost(1L,1L));
    }
}
