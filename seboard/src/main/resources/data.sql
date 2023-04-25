INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile,created_at) values (100,'user','user','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','20180167@kumoh.ac.kr','none','2023-04-26');
INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile,created_at) values (200,'admin','admin','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','hanna@kumoh.ac.kr','none','2023-04-26');
INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile,created_at) values (300,'professor','professor','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','halee@kumoh.ac.kr','none','2023-04-26');
INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile,created_at) values (400,'kumoh','maanjong','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','alswhd1113@kumoh.ac.kr','none','2023-04-26');
INSERT INTO roles (role_id, name) values (1, 'ROLE_USER'),(2, 'ROLE_ADMIN'),(3, 'ROLE_PROFESSOR'),(4, 'ROLE_KUMOH');

INSERT INTO authorities(account_id,role_id) values(100,1);
INSERT INTO authorities(account_id,role_id) values(200,2);
INSERT INTO authorities(account_id,role_id) values(300,3);
INSERT INTO authorities(account_id,role_id) values(400,4);
INSERT INTO public.authorization_meta_data(id, method_signature, role_id) VALUES (1, '/admin', 2);