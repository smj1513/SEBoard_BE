BEGIN;

INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile,created_at) values (5234058023850,'user','user','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','20180167@kumoh.ac.kr','none','2023-04-26');
INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile,created_at) values (5234058023851,'admin','admin','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','hanna@kumoh.ac.kr','none','2023-04-26');
INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile,created_at) values (5234058023852,'professor','professor','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','halee@kumoh.ac.kr','none','2023-04-26');
INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile,created_at) values (5234058023853,'kumoh','maanjong','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','lcy@kumoh.ac.kr','none','2023-04-26');
INSERT INTO roles (role_id, name) values (5234058023850, 'ROLE_USER'),(5234058023851, 'ROLE_ADMIN'),(5234058023852, 'ROLE_PROFESSOR'),(5234058023853, 'ROLE_KUMOH');

INSERT INTO authorities(account_id,role_id) values(5234058023850,5234058023850);
INSERT INTO authorities(account_id,role_id) values(5234058023851,5234058023851);
INSERT INTO authorities(account_id,role_id) values(5234058023852,5234058023852);
INSERT INTO authorities(account_id,role_id) values(5234058023853,5234058023853);

INSERT INTO public.authorizations(id, method, path, priority) VALUES (1543, 'GET', '/admin/**', 9999);
INSERT INTO public.authorizations(id, method, path, priority) VALUES (1544, 'POST', '/admin/**', 9999);
INSERT INTO public.authorizations(id, method, path, priority) VALUES (1545, 'PUT', '/admin/**', 9999);
INSERT INTO public.authorizations(id, method, path, priority) VALUES (1546, 'DELETE', '/admin/**', 9999);

INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243124,1543, 5234058023851);
INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243125,1544, 5234058023851);
INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243126,1545, 5234058023851);
INSERT INTO public.authorization_metadata(id,authorization_id, role_id) VALUES (3121243127,1546, 5234058023851);

INSERT INTO categories(category_id,super_category_id,name) values (43214232,null,'공지사항');
INSERT INTO categories(category_id,super_category_id,name) values (43214233,43214232,'일반');
INSERT INTO categories(category_id,super_category_id,name) values (43214234,43214232,'강의');
INSERT INTO categories(category_id,super_category_id,name) values (43214235,43214232,'학사');
INSERT INTO categories(category_id,super_category_id,name) values (43214236,43214232,'학생회');

INSERT INTO public.expose_options(dtype, expose_option_id, expose_state) VALUES ('Public', 10001, 'PUBLIC');
INSERT INTO public.expose_options(dtype, expose_option_id, expose_state) VALUES ('Kumoh', 10002, 'KUMOH');
INSERT INTO public.expose_options(dtype, expose_option_id, expose_state) VALUES ('Privacy', 10003, 'PRIVACY');
INSERT INTO public.publics(expose_option_id) VALUES (10001);
INSERT INTO public.kumohs(expose_option_id) VALUES (10002);
INSERT INTO public.privacies(password, expose_option_id) VALUES ('1234', 10003);

INSERT INTO public.board_users(board_user_id, name) VALUES (3421243, '이충엽');
INSERT INTO public.members(member_id, account_id) VALUES (3421243, 5234058023853);
INSERT INTO public.board_users(board_user_id, name) VALUES (3421244, '익명의 사나이');
INSERT INTO public.anonymous(account_id, anonymous_id) VALUES (5234058023852, 3421244);

INSERT INTO public.board_users(board_user_id, name) VALUES (3421245, '이한나');
INSERT INTO public.members(member_id, account_id) VALUES (3421245, 5234058023851);

INSERT INTO public.board_users(board_user_id, name) VALUES (3421246, '멋지다 충엽아');
INSERT INTO public.anonymous(account_id, anonymous_id) VALUES (5234058023850, 3421246);

INSERT INTO public.posts(
    post_id, anonymous_count, created_at, modified_at, contents, pined, title, views, board_user_id, category_id, expose_option_id)
VALUES (1234879892103, 1, '2023-04-24', '2023-04-26','<div class="document_91563_19198 xe_content"><p>&nbsp;</p>
<p><span style="font-size:18px;">안녕하십니까&nbsp;18학번 이충엽입니다&nbsp;</span></p>
  <p><span style="font-size:18px;">저는 5월 1일 14시부로 논산 훈련소에 입대 예정입니다.&nbsp;</span></p>
  <p><span style="font-size:18px;">그동안 감사했습니다! 충성!</span></p></div>' ,
        true, '18이충엽 재입대 합니다.', 123,3421243 , 43214233, 10001);

INSERT INTO public.bookmarks(bookmark_id, post_id, member_id) VALUES (534453, 1234879892103, 3421243);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
VALUES (2342343,'2023-04-13', '2023-04-14', '이충엽씨 축하드립니다~', FALSE, 3421244, FALSE, 1234879892103);

    INSERT INTO public.comments(
        comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
    VALUES (2342345,'2023-04-14', '2023-04-14', '앞으로의 행보 응원합니다!', FALSE, 3421246, FALSE, 1234879892103);
    INSERT INTO public.replies(comment_id, super_comment_id, tag_comment_id) VALUES (2342345, 2342343, 2342343);

    INSERT INTO public.comments(
        comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
    VALUES (2342346,'2023-04-15', '2023-04-15', '응원 감사합니다!', FALSE, 3421243, FALSE, 1234879892103);
    INSERT INTO public.replies(comment_id, super_comment_id, tag_comment_id) VALUES (2342346, 2342343, 2342345);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
VALUES (2342344,'2023-04-13', '2023-04-13', '축하해 충엽아~ 휴학계 잊지말고~', FALSE, 3421245, TRUE, 1234879892103);
    INSERT INTO public.comments(
        comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
    VALUES (2342347,'2023-04-14', '2023-04-14', '네 꼭 챙기겠습니다~', FALSE, 3421243, TRUE, 1234879892103);
    INSERT INTO public.replies(comment_id, super_comment_id, tag_comment_id) VALUES (2342347, 2342344, 2342344);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
VALUES (2342348,'2023-04-16', '2023-04-16', 'ㅎㅇ 난 삭제된 댓글', TRUE, 3421245, FALSE, 1234879892103);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
VALUES (2342349,'2023-04-17', '2023-04-17', 'ㅎㅇ 난 비밀 댓글', FALSE, 3421245, FALSE, 1234879892103);


INSERT INTO public.posts(
    post_id, anonymous_count, created_at, modified_at, contents, pined, title, views, board_user_id, category_id, expose_option_id)
VALUES (1234879892104, 1, '2023-04-25', '2023-04-28','<div class="document_91563_19198 xe_content"><p>&nbsp;</p>
<p><span style="font-size:18px;">ㅈㄱㄴ&nbsp;</span></p></div>' ,
        true, '옥계 참숯고기 어떤가요?', 9999,3421244 , 43214233, 10001);

INSERT INTO public.comments(
    comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
VALUES (2342350,'2023-04-27', '2023-04-27', '전 괜찮은데 금슐랭 박형준씨는 별로 안좋아하더라구요', FALSE, 3421245, FALSE, 1234879892104);
    INSERT INTO public.comments(
        comment_id, created_at, modified_at, contents, is_deleted, board_user_id, is_only_read_by_author, post_id)
    VALUES (2342351,'2023-04-28', '2023-04-28', '좋은 정보 감사합니다!', FALSE, 3421244, FALSE, 1234879892104);
    INSERT INTO public.replies(comment_id, super_comment_id, tag_comment_id) VALUES (2342351, 2342350, 2342350);


INSERT INTO public.posts(
    post_id, anonymous_count, created_at, modified_at, contents, pined, title, views, board_user_id, category_id, expose_option_id)
VALUES (1234879892105, 1, '2023-04-25', '2023-04-28','<script>alert("xss")</script>' ,
        true, 'xss 테스트', 9999,3421244 , 43214233, 10001);


-- INSERT INTO public.ip(id, ip_address) VALUES (5343452, '127.0.0.1');
-- INSERT INTO public.ip(id, ip_address) VALUES (5343453, '0:0:0:0:0:0:0:1');

COMMIT