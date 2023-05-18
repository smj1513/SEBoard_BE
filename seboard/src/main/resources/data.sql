BEGIN;

INSERT INTO accounts(account_id,login_id,name,nickname,password,created_at) values (5234058023850,'user','username','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','2023-04-26');
INSERT INTO accounts(account_id,login_id,name,nickname,password,created_at) values (5234058023851,'admin','admin','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','2023-04-26');
INSERT INTO accounts(account_id,login_id,name,nickname,password,created_at) values (5234058023852,'professor','professor','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','2023-04-26');
INSERT INTO accounts(account_id,login_id,name,nickname,password,created_at) values (5234058023853,'kumoh','maanjong','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','2023-04-26');
INSERT INTO roles (role_id, name,description,alias) values (5234058023850, 'ROLE_USER','일반 사용자,일부 게시글에 접근 가능','준회원'),
                                                           (5234058023851, 'ROLE_ADMIN', '최고 관리 권한을 가짐','관리자'),
                                                           (5234058023852, 'ROLE_PROFESSOR','교수 권한','교수'),
                                                           (5234058023853, 'ROLE_KUMOH','금오공대 이메일 인증을 하여 금오인에게 공개된 게시글에 접근 가능','금오인');
INSERT INTO public.accounts(account_id, created_at, login_id, name, nickname, password)
VALUES (5234058023854, '2023-05-13 17:35:53.152235', 'alswhd1113@gmail.com', '김민종', '312', '$2a$10$NEg.I3iB2foXjjY4fXXgS.Zeu9HJlxdVnO9n2GTK1hga1l8.2Vkd6');
INSERT INTO public.oauth_accounts(id, provider, sub, account_id) VALUES (2, 'kakao', '2732852527', 5234058023854);

INSERT INTO authorities(account_id,role_id) values(5234058023850,5234058023850);
INSERT INTO authorities(account_id,role_id) values(5234058023851,5234058023851);
INSERT INTO authorities(account_id,role_id) values(5234058023852,5234058023852);
INSERT INTO authorities(account_id,role_id) values(5234058023853,5234058023853);
INSERT INTO authorities(account_id,role_id) values(5234058023854,5234058023850);

INSERT INTO menus(menu_id, super_menu_id, name, description, depth, url_info, menu_type) VALUES
                                                                                             (43214231, null, '공지사항', '공지임니둥', 0, 'notice', 'BOARD'),
                                                                                             (43214232, 43214231, '일반', '공지임니둥', 1, 'notice_normal', 'CATEGORY'),
                                                                                             (43214233, 43214231, '수업', '공지임니둥', 1, 'class', 'CATEGORY'),
                                                                                             (43214234, 43214231, '장학금', '공지임니둥', 1, 'money', 'CATEGORY'),
                                                                                             (43214235, 43214231, '학사', '공지임니둥', 1, 'student', 'CATEGORY'),
                                                                                             (43214238, null, '자유게시판', '자유이니둥', 0, 'freeboard', 'BOARD'),
                                                                                             (43214239, 43214238, '일반', '자유이니둥', 1, 'freeboard_normal', 'BOARD'),
                                                                                             (43214240, 43214238, '전공지식', '자유이니둥', 1, 'knwoledge', 'BOARD'),
                                                                                             (43214236, null, '채용', '채용임니둥', 0, 'https://cs.kumoh.ac.kr/cs/sub0602.do', 'EXTERNAL'),
                                                                                             (43214237, null, '학사', '학사임니둥', 0, 'https://cs.kumoh.ac.kr/cs/sub0601.do', 'EXTERNAL');

INSERT INTO public.authorizations(id, method, path,dtype) VALUES (1543, 'GET', '/admin/**','ADMIN');
INSERT INTO public.authorizations(id, method, path,dtype) VALUES (1544, 'POST', '/admin/**','ADMIN');
INSERT INTO public.authorizations(id, method, path,dtype) VALUES (1545, 'PUT', '/admin/**','ADMIN');
INSERT INTO public.authorizations(id, method, path,dtype) VALUES (1546, 'DELETE', '/admin/**','ADMIN');
INSERT INTO public.authorizations(id, method, path,dtype) VALUES (1547, 'GET', '/category/notice_normal/posts/**','CATEGORY');
INSERT INTO public.authorizations(id, method, path,dtype) VALUES (1548, 'POST', '/category/notice_normal/posts/**','CATEGORY');
INSERT INTO public.authorizations(id, method, path,dtype) VALUES (1549, 'PUT', '/category/notice_normal/posts/**','CATEGORY');
INSERT INTO public.authorizations(id, method, path,dtype) VALUES (1550, 'DELETE', '/category/notice_normal/posts/**','CATEGORY');

INSERT INTO public.admin_authorizations(id) VALUES (1543);
INSERT INTO public.admin_authorizations(id) VALUES (1544);
INSERT INTO public.admin_authorizations(id) VALUES (1545);
INSERT INTO public.admin_authorizations(id) VALUES (1546);

INSERT INTO public.category_authorizations(access_type, id, category_id) VALUES ('READ', 1547, 43214232);
INSERT INTO public.category_authorizations(access_type, id, category_id) VALUES ('CREATE', 1548, 43214232);
INSERT INTO public.category_authorizations(access_type, id, category_id) VALUES ('UPDATE', 1549, 43214232);
INSERT INTO public.category_authorizations(access_type, id, category_id) VALUES ('DELETE', 1550, 43214232);

INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243124,1543, 5234058023851);
INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243125,1544, 5234058023851);
INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243126,1545, 5234058023851);
INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243127,1546, 5234058023851);


INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243128,1547, 5234058023851);

INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243129,1548, 5234058023850);
INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243130,1548, 5234058023851);

INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243131,1549, 5234058023851);

INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243132,1550, 5234058023850);
INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243133,1550, 5234058023851);



-- INSERT INTO public.authorizations(id, method, path, priority) VALUES (1547, 'POST', '/posts/**', 9999);
-- INSERT INTO public.authorizations(id, method, path, priority) VALUES (1548, 'PUT', '/posts/**', 9999);
-- INSERT INTO public.authorizations(id, method, path, priority) VALUES (1549, 'DELETE', '/posts/**', 9999);
--
-- INSERT INTO public.authorizations(id, method, path, priority) VALUES (1550, 'POST', '/comments/**', 9999);
-- INSERT INTO public.authorizations(id, method, path, priority) VALUES (1551, 'PUT', '/comments/**', 9999);
-- INSERT INTO public.authorizations(id, method, path, priority) VALUES (1552, 'DELETE', '/comments/**', 9999);



--
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243128,1547, 5234058023850);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243129,1547, 5234058023851);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243130,1547, 5234058023853);
--
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243131,1548, 5234058023850);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243132,1548, 5234058023851);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243133,1548, 5234058023853);
--
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243134,1549, 5234058023850);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243135,1549, 5234058023851);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243136,1549, 5234058023853);
--
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243137,1550, 5234058023850);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243138,1550, 5234058023851);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243139,1550, 5234058023853);
--
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243140,1551, 5234058023850);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243141,1551, 5234058023851);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243142,1551, 5234058023853);
--
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243143,1552, 5234058023850);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243144,1552, 5234058023851);
-- INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243145,1552, 5234058023853);


INSERT INTO public.board_users(board_user_id, name) VALUES (3421243, '이충엽');
INSERT INTO public.members(member_id, account_id) VALUES (3421243, 5234058023853);
INSERT INTO public.board_users(board_user_id, name) VALUES (3421244, '익명의 사나이');
INSERT INTO public.anonymous(account_id, anonymous_id) VALUES (5234058023852, 3421244);

INSERT INTO public.board_users(board_user_id, name) VALUES (3421245, '이한나');
INSERT INTO public.members(member_id, account_id) VALUES (3421245, 5234058023851);

INSERT INTO public.board_users(board_user_id, name) VALUES (3421246, '멋지다 충엽아');
INSERT INTO public.anonymous(account_id, anonymous_id) VALUES (5234058023850, 3421246);

INSERT INTO expose_options(expose_option_id, expose_type) VALUES (28822821, 'PUBLIC');
INSERT INTO expose_options(expose_option_id, expose_type) VALUES (28822822, 'KUMOH');
INSERT INTO expose_options(expose_option_id, expose_type) VALUES (28822823, 'PUBLIC');

INSERT INTO public.posts(
    post_id, anonymous_count, created_at, modified_at, contents, pined, title, views, board_user_id, category_id, expose_option_id, status, report_count)
VALUES (1234879892103, 1, '2023-04-24', '2023-04-26','<div class="document_91563_19198 xe_content"><p>&nbsp;</p>
<p><span style="font-size:18px;">안녕하십니까&nbsp;18학번 이충엽입니다&nbsp;</span></p>
  <p><span style="font-size:18px;">저는 5월 1일 14시부로 논산 훈련소에 입대 예정입니다.&nbsp;</span></p>
  <p><span style="font-size:18px;">그동안 감사했습니다! 충성!</span></p></div>' ,
        true, '18이충엽 재입대 합니다.', 123,3421243 , 43214232, 28822821, 'NORMAL', 0);

INSERT INTO public.bookmarks(bookmark_id, post_id, member_id) VALUES (534453, 1234879892103, 3421243);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, status, board_user_id, is_only_read_by_author, post_id, comment_type, report_count)
VALUES (2342343,'2023-04-13', '2023-04-14', '이충엽씨 축하드립니다~', 'NORMAL', 3421244, FALSE, 1234879892103, 'comment', 0);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, status, board_user_id, is_only_read_by_author, post_id, super_comment_id, tag_comment_id, comment_type, report_count)
VALUES (2342345,'2023-04-14', '2023-04-14', '앞으로의 행보 응원합니다!', 'NORMAL', 3421246, FALSE, 1234879892103, 2342343, 2342343, 'reply', 0);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, status, board_user_id, is_only_read_by_author, post_id, super_comment_id, tag_comment_id, comment_type, report_count)
VALUES (2342346,'2023-04-15', '2023-04-15', '응원 감사합니다!', 'NORMAL', 3421243, FALSE, 1234879892103, 2342343, 2342345, 'reply', 0);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, status, board_user_id, is_only_read_by_author, post_id, comment_type, report_count)
VALUES (2342344,'2023-04-13', '2023-04-13', '축하해 충엽아~ 휴학계 잊지말고~', 'NORMAL', 3421245, TRUE, 1234879892103, 'comment', 0);
INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, status, board_user_id, is_only_read_by_author, post_id, super_comment_id, tag_comment_id, comment_type, report_count)
VALUES (2342347,'2023-04-14', '2023-04-14', '네 꼭 챙기겠습니다~', 'NORMAL', 3421243, TRUE, 1234879892103, 2342344, 2342344, 'reply', 0);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, status, board_user_id, is_only_read_by_author, post_id, comment_type, report_count)
VALUES (2342348,'2023-04-16', '2023-04-16', 'ㅎㅇ 난 삭제된 댓글', 'NORMAL', 3421245, FALSE, 1234879892103, 'comment', 0);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, status, board_user_id, is_only_read_by_author, post_id, comment_type, report_count)
VALUES (2342349,'2023-04-17', '2023-04-17', 'ㅎㅇ 난 비밀 댓글', 'NORMAL', 3421245, FALSE, 1234879892103, 'comment', 0);


INSERT INTO public.posts(
    post_id, anonymous_count, created_at, modified_at, contents, pined, title, views, board_user_id, category_id, expose_option_id, status, report_count)
VALUES (1234879892104, 1, '2023-04-25', '2023-04-28','<div class="document_91563_19198 xe_content"><p>&nbsp;</p>
<p><span style="font-size:18px;">ㅈㄱㄴ&nbsp;</span></p></div>' ,
        true, '옥계 참숯고기 어떤가요?', 9999,3421244 , 43214232, 28822822, 'NORMAL', 0);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, status, board_user_id, is_only_read_by_author, post_id, comment_type, report_count)
VALUES (2342350,'2023-04-27', '2023-04-27', '전 괜찮은데 금슐랭 박형준씨는 별로 안좋아하더라구요', 'NORMAL', 3421245, FALSE, 1234879892104, 'comment', 0);


INSERT INTO public.posts(
    post_id, anonymous_count, created_at, modified_at, contents, pined, title, views, board_user_id, category_id, expose_option_id, status, report_count)
VALUES (1234879892105, 1, '2023-04-25', '2023-04-28','<script>alert("xss")</script>' ,
        false, 'xss 테스트', 9999,3421244 , 43214232, 28822823, 'NORMAL', 0);

-- INSERT INTO public.ip(id, ip_address) VALUES (5343452, '127.0.0.1');
-- INSERT INTO public.ip(id, ip_address) VALUES (5343453, '0:0:0:0:0:0:0:1');

COMMIT