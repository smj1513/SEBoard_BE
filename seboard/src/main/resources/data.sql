INSERT INTO roles (role_id, name) values (1, 'ROLE_USER'),(2, 'ROLE_ADMIN'),(3, 'ROLE_MANAGER'),(4, 'ROLE_PROFESSOR');
INSERT INTO accounts(account_id,login_id,username,nickname,password,provider,email,profile) values (1,'user','user','nick','$2a$10$Dw5746fmIzeN.SqjuPzR9.FHEwQP4IXOggdIG78bjaWn1lz0864R6','se','20180167@kumoh.ac.kr','none');
INSERT INTO authorities(account_id,role_id) values(1,1);