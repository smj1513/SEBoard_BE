package com.seproject.board.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.seproject.account.account.domain.Account;
import com.seproject.account.account.domain.FormAccount;
import com.seproject.admin.domain.SelectOption;
import com.seproject.admin.menu.application.AdminMenuAppService;
import com.seproject.admin.menu.controller.dto.MenuDTO;
import com.seproject.admin.menu.utils.MenuRequestBuilder;
import com.seproject.board.common.Status;
import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.menu.domain.Category;
import com.seproject.board.menu.domain.Menu;
import com.seproject.board.post.controller.dto.ExposeOptionRequest;
import com.seproject.board.post.controller.dto.PostRequest;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.model.exposeOptions.ExposeOption;
import com.seproject.board.post.domain.model.exposeOptions.ExposeState;
import com.seproject.board.post.service.PostService;
import com.seproject.global.*;
import com.seproject.member.domain.BoardUser;
import com.seproject.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired EntityManager em;

    @Autowired PostService postService;

    @Autowired PostSetup postSetup;
    @Autowired MenuSetup menuSetup;
    @Autowired BoardUserSetup boardUserSetup;
    @Autowired AccountSetup accountSetup;
    @Autowired TokenSetup tokenSetup;
    @Autowired RoleSetup roleSetup;
    @Autowired CommentSetup commentSetup;

    @Autowired AdminMenuAppService adminMenuAppService;

    static final String url = "/posts/";
    
    @Test
    public void 게시글_목록_조회() throws Exception {

        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        for (int i = 0; i < 43; i++) {
            postSetup.createPost(member, category);
        }

        em.flush(); em.clear();

        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .queryParam("categoryId", String.valueOf(category.getMenuId()))
                .queryParam("page" , "0")
                .queryParam("perPage", "50")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(43))
                .andExpect(jsonPath("$.empty").value(false))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.totalElements").value(43))
                .andExpect(jsonPath("$.numberOfElements").value(43))
                .andExpect(jsonPath("$.size").value(50));
    }
    @Test
    public void 게시글_카테고리_비로그인_권한_없음() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.createRole().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        em.flush(); em.clear();

        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .queryParam("categoryId", String.valueOf(category.getMenuId()))
                .queryParam("page" , "0")
                .queryParam("perPage", "50")
        ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_카테고리_로그인_권한_없음() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.createRole().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        em.flush(); em.clear();

        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .queryParam("categoryId", String.valueOf(category.getMenuId()))
                        .queryParam("page" , "0")
                        .queryParam("perPage", "50")
                        .header("Authorization",tokenSetup.getAccessToken(adminAccount))
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_상세_조회() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        Post post = postSetup.createPost(member, category);

        em.flush(); em.clear();

        mvc.perform(get(url + post.getPostId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenSetup.getAccessToken(formAccount))
                .characterEncoding("UTF-8")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getPostId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.contents").value(post.getContents()))
                .andExpect(jsonPath("$.category.categoryId").value(category.getMenuId()))
                .andExpect(jsonPath("$.category.name").value(category.getName()))
                .andExpect(jsonPath("$.author.loginId").value(member.getLoginId()))
                .andExpect(jsonPath("$.author.name").value(member.getName()))
                .andExpect(jsonPath("$.views").value(post.getViews()));

    }
    @Test
    public void 게시글_상세_조회_비로그인_권한_없음() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        Post post = postSetup.createPost(member, category);
        post.changeExposeOption(ExposeOption.of(ExposeState.KUMOH,null));

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.getRoleAdmin().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();
        Member adminMember = boardUserSetup.createMember(adminAccount);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        mvc.perform(get(url + post.getPostId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void 게시글_상세_조회_로그인_권한_없음() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        FormAccount reader = accountSetup.createFormAccount();
        Member readerMember = boardUserSetup.createMember(reader);

        Post post = postSetup.createPost(member, category);
        post.changeExposeOption(ExposeOption.of(ExposeState.KUMOH,null));

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.createRole().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        mvc.perform(get(url + post.getPostId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Authorization",tokenSetup.getAccessToken(reader))
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_상세_조회_권한_없어도_내가_작성한_글은_조회_가능() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        Post post = postSetup.createPost(member, category);
        post.changeExposeOption(ExposeOption.of(ExposeState.KUMOH,null));

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.createRole().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        mvc.perform(get(url + post.getPostId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Authorization",tokenSetup.getAccessToken(formAccount))
                ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 비밀글_상세_조회() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        FormAccount reader = accountSetup.createFormAccount();
        Member readerMember = boardUserSetup.createMember(reader);

        Post post = postSetup.createPost(member, category);
        String password = UUID.randomUUID().toString();
        post.changeExposeOption(ExposeOption.of(ExposeState.PRIVACY,password));

        PostRequest.RetrievePrivacyPostRequest request = new PostRequest.RetrievePrivacyPostRequest();
        request.setPassword(password);

        em.flush(); em.clear();

        mvc.perform(post(url + post.getPostId() + "/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", tokenSetup.getAccessToken(reader))
                .content(objectMapper.writeValueAsString(request))
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getPostId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.contents").value(post.getContents()))
                .andExpect(jsonPath("$.category.categoryId").value(category.getMenuId()))
                .andExpect(jsonPath("$.category.name").value(category.getName()))
                .andExpect(jsonPath("$.author.loginId").value(member.getLoginId()))
                .andExpect(jsonPath("$.author.name").value(member.getName()));
    }
    @Test
    public void 비밀글_상세_조회_비밀번호_틀림() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        FormAccount reader = accountSetup.createFormAccount();
        Member readerMember = boardUserSetup.createMember(reader);

        Post post = postSetup.createPost(member, category);
        String password = UUID.randomUUID().toString();
        post.changeExposeOption(ExposeOption.of(ExposeState.PRIVACY,password));

        PostRequest.RetrievePrivacyPostRequest request = new PostRequest.RetrievePrivacyPostRequest();
        request.setPassword("1234");

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.getRoleAdmin().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();
        Member adminMember = boardUserSetup.createMember(adminAccount);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        mvc.perform(post(url + post.getPostId() + "/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Authorization", tokenSetup.getAccessToken(reader))
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void 비밀글_상세_조회_관리자() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        FormAccount reader = accountSetup.createFormAccount();
        Member readerMember = boardUserSetup.createMember(reader);

        Post post = postSetup.createPost(member, category);
        String password = UUID.randomUUID().toString();
        post.changeExposeOption(ExposeOption.of(ExposeState.PRIVACY,password));

        PostRequest.RetrievePrivacyPostRequest request = new PostRequest.RetrievePrivacyPostRequest();
        request.setPassword("1234");

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.getRoleAdmin().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();
        Member adminMember = boardUserSetup.createMember(adminAccount);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);


        em.flush(); em.clear();

        mvc.perform(post(url + post.getPostId() + "/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Authorization", tokenSetup.getAccessToken(adminAccount))
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 비밀글_상세_조회_작성자() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        FormAccount reader = accountSetup.createFormAccount();
        Member readerMember = boardUserSetup.createMember(reader);

        Post post = postSetup.createPost(member, category);
        String password = UUID.randomUUID().toString();
        post.changeExposeOption(ExposeOption.of(ExposeState.PRIVACY,password));

        PostRequest.RetrievePrivacyPostRequest request = new PostRequest.RetrievePrivacyPostRequest();
        request.setPassword("1234");

        em.flush(); em.clear();

        mvc.perform(post(url + post.getPostId() + "/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Authorization", tokenSetup.getAccessToken(reader))
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 공지_게시글_조회() throws Exception {
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        for (int i = 0; i < 4; i++) {
            Post post = postSetup.createPost(member, category);
            post.changePin(true);
        }

        em.flush(); em.clear();

        mvc.perform(get(url + "pined")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .queryParam("categoryId",String.valueOf(category.getMenuId()))
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4));
    }
    @Test
    public void 공지_게시글_조회_권한_없음() throws Exception {
        Menu menu = menuSetup.createMenu();
        BoardMenu boardMenu = menuSetup.createBoardMenu(menu);
        Category category = menuSetup.createCategory(boardMenu);

        MenuDTO.MenuAuthOption expose = new MenuDTO.MenuAuthOption();
        expose.setOption(SelectOption.SELECT.getName());
        expose.setRoles(List.of(roleSetup.getRoleAdmin().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {expose,expose,expose,expose};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(menu.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        mvc.perform(get(url + "pined")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .queryParam("categoryId",String.valueOf(category.getMenuId()))
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_작성() throws Exception {

        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        em.flush(); em.clear();

        PostRequest.CreatePostRequest request = new PostRequest.CreatePostRequest();
        request.setTitle(UUID.randomUUID().toString());
        request.setContents(UUID.randomUUID().toString());
        request.setCategoryId(category.getMenuId());
        request.setPined(false);

        request.setExposeOption(new ExposeOptionRequest("PUBLIC"));
        request.setAnonymous(false);

        String result = mvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", tokenSetup.getAccessToken(formAccount))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse()
                .getContentAsString();

        em.flush(); em.clear();

        Integer id = JsonPath.parse(result).read("$.id");
        Post findPost = postService.findById((long)id);

        Assertions.assertEquals(findPost.getTitle(),request.getTitle());
        Assertions.assertEquals(findPost.getContents(),request.getContents());
        Assertions.assertEquals(findPost.getCategory().getMenuId(),request.getCategoryId());
        Assertions.assertEquals(findPost.isPined(),request.isPined());
        Assertions.assertEquals(findPost.getExposeOption().getExposeState().name(),request.getExposeOption().getName());
        Assertions.assertTrue(findPost.isNamed());

        BoardUser author = findPost.getAuthor();
        Assertions.assertFalse(author.isAnonymous());
        Assertions.assertTrue(author.isOwnAccountId(formAccount.getAccountId()));

    }
    @Test
    public void 게시글_작성_권한_없음() throws Exception {

        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.getRoleAdmin().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        PostRequest.CreatePostRequest request = new PostRequest.CreatePostRequest();
        request.setTitle(UUID.randomUUID().toString());
        request.setContents(UUID.randomUUID().toString());
        request.setCategoryId(category.getMenuId());
        request.setPined(false);

        request.setExposeOption(new ExposeOptionRequest("PUBLIC"));
        request.setAnonymous(false);

        mvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", tokenSetup.getAccessToken(formAccount))
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_작성_금오_권한_없음() throws Exception {

        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        em.flush(); em.clear();

        PostRequest.CreatePostRequest request = new PostRequest.CreatePostRequest();
        request.setTitle(UUID.randomUUID().toString());
        request.setContents(UUID.randomUUID().toString());
        request.setCategoryId(category.getMenuId());
        request.setPined(false);

        request.setExposeOption(new ExposeOptionRequest("KUMOH"));
        request.setAnonymous(false);

        mvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", tokenSetup.getAccessToken(formAccount))
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_수정() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        Post post = postSetup.createPost(member, category);

        em.flush(); em.clear();

        PostRequest.UpdatePostRequest request = new PostRequest.UpdatePostRequest();
        request.setTitle(UUID.randomUUID().toString());
        request.setContents(UUID.randomUUID().toString());
        request.setCategoryId(category.getMenuId());
        request.setPined(false);
        request.setExposeOption(new ExposeOptionRequest("PRIVACY","1234"));

        mvc.perform(put(url + post.getPostId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",tokenSetup.getAccessToken(formAccount))
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        ).andDo(print())
                .andExpect(status().isOk());

        em.flush(); em.clear();

        Post findPost = postService.findById(post.getPostId());

        Assertions.assertEquals(request.getTitle(),findPost.getTitle());
        Assertions.assertEquals(request.getContents(),findPost.getContents());
        Assertions.assertEquals(request.getCategoryId(),findPost.getCategory().getMenuId());
        Assertions.assertFalse(request.isPined());
        Assertions.assertEquals("PRIVACY",findPost.getExposeOption().getExposeState().name());
        Assertions.assertEquals("1234",findPost.getExposeOption().getPassword());
    }
    @Test
    public void 게시글_수정_내가_작성한거_아님() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        Post post = postSetup.createPost(member, category);

        Account adminAccount = accountSetup.getAdminAccount();
        em.flush(); em.clear();

        PostRequest.UpdatePostRequest request = new PostRequest.UpdatePostRequest();
        request.setTitle(UUID.randomUUID().toString());
        request.setContents(UUID.randomUUID().toString());
        request.setCategoryId(category.getMenuId());
        request.setPined(false);
        request.setExposeOption(new ExposeOptionRequest("PRIVACY","1234"));

        mvc.perform(put(url + post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization",tokenSetup.getAccessToken(adminAccount))
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding("UTF-8")
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_수정_권한_없음() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        Post post = postSetup.createPost(member, category);

        MenuDTO.MenuAuthOption edit = new MenuDTO.MenuAuthOption();
        edit.setOption(SelectOption.SELECT.getName());
        edit.setRoles(List.of(roleSetup.getRoleAdmin().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,edit,edit};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        PostRequest.UpdatePostRequest request = new PostRequest.UpdatePostRequest();
        request.setTitle(UUID.randomUUID().toString());
        request.setContents(UUID.randomUUID().toString());
        request.setCategoryId(category.getMenuId());
        request.setPined(false);
        request.setExposeOption(new ExposeOptionRequest("PRIVACY","1234"));

        mvc.perform(put(url + post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization",tokenSetup.getAccessToken(formAccount))
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding("UTF-8")
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_삭제() throws Exception {

        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        Post post = postSetup.createPost(member, category);


        em.flush(); em.clear();

        mvc.perform(delete(url + post.getPostId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization",tokenSetup.getAccessToken(formAccount))
        ).andDo(print())
                .andExpect(status().isOk());

        em.flush(); em.clear();

        Post deletePost = postService.findById(post.getPostId());

        Assertions.assertEquals(deletePost.getStatus(), Status.PERMANENT_DELETED);
    }
    @Test
    public void 게시글_삭제_작성자도_권한도_없음() throws Exception {

        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        Post post = postSetup.createPost(member, category);

        MenuDTO.MenuAuthOption manage = new MenuDTO.MenuAuthOption();
        manage.setOption(SelectOption.SELECT.getName());
        manage.setRoles(List.of(roleSetup.createRole().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,manage,manage};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        mvc.perform(delete(url + post.getPostId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Authorization",tokenSetup.getAccessToken(adminAccount))
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void 게시글_삭제_권한_있음() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);
        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));
        Post post = postSetup.createPost(member, category);

        MenuDTO.MenuAuthOption manage = new MenuDTO.MenuAuthOption();
        manage.setOption(SelectOption.SELECT.getName());
        manage.setRoles(List.of(roleSetup.getRoleAdmin().getId()));

        MenuDTO.MenuAuthOption[] input = new MenuDTO.MenuAuthOption[] {null,null,manage,manage};
        MenuDTO.UpdateMenuRequest req = MenuRequestBuilder.getUpdateMenuRequest(input);

        Account adminAccount = accountSetup.getAdminAccount();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminAccount,
                        null,adminAccount.getAuthorities()));

        adminMenuAppService.update(category.getMenuId(),req);

        SecurityContextHolder.getContext().setAuthentication(null);

        em.flush(); em.clear();

        mvc.perform(delete(url + post.getPostId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Authorization",tokenSetup.getAccessToken(adminAccount))
                ).andDo(print())
                .andExpect(status().isOk());

        Post deletePost = postService.findById(post.getPostId());

        Assertions.assertEquals(deletePost.getStatus(),Status.PERMANENT_DELETED);
    }

    @Test
    public void 게시글_댓글_조회() throws Exception {

        FormAccount formAccount = accountSetup.createFormAccount();
        Member member = boardUserSetup.createMember(formAccount);

        Category category = menuSetup.createCategory(menuSetup.createBoardMenu(menuSetup.createMenu()));

        Post post = postSetup.createPost(member, category);

        for (int i = 0; i < 4; i++) {
            commentSetup.createComment(post,member);
        }

        em.flush(); em.clear();

        mvc.perform(get(url + post.getPostId() + "/comments")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(4));
    }





















}