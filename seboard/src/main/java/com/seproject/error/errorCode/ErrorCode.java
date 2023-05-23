package com.seproject.error.errorCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_JWT(HttpStatus.UNAUTHORIZED,100, "유효하지 않은 Json Web Token입니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST,101, "비밀번호가 틀렸습니다."),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED,102, "로그인이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN,103, "접근 권한이 존재하지 않습니다."),
    BANNED_IP(HttpStatus.FORBIDDEN,104, "금지된 ip입니다."),
    NOT_REGISTERED_USER(HttpStatus.TEMPORARY_REDIRECT,105, "추가 정보를 입력하지 않은 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED,106, "존재하지 않는 아이디입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,107,"존재하지 않는 refreshToken 입니다."),
    TOKEN_EXPIRED(HttpStatus.NOT_FOUND,108,"만료된 Token 입니다."),
    ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST,109,"존재하지 않는 권한을 요청하였습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST,110,"이미 존재하는 id입니다."),
    INVALID_MAIL(HttpStatus.BAD_REQUEST,111,"잘못된 이메일 형식입니다."),
    INCORRECT_AUTH_CODE(HttpStatus.BAD_REQUEST,112,"인증 코드가 일치하지 않습니다."),
    NOT_POST_AUTHOR(HttpStatus.BAD_REQUEST,113,"비밀 게시글 작성자가 아닙니다."),
    INCORRECT_POST_PASSWORD(HttpStatus.BAD_REQUEST,114,"게시글 비밀번호가 일치하지 않습니다."),
    EXCEED_WRITING_COUNT(HttpStatus.BAD_REQUEST,115,"게시글 및 댓글 작성 횟수 초과입니다."),
    EMAIL_NOT_FOUNT(HttpStatus.NOT_FOUND,116,"일치하는 이메일 인증 정보가 없습니다."),
    ALREADY_EXIST_ROLE(HttpStatus.BAD_REQUEST,117,"이미 존재하는 권한 정보입니다."),
    IMMUTABLE_ROLE(HttpStatus.BAD_REQUEST,118,"삭제할수 없는 권한 정보입니다."),
    LOGIN_PREVENT_USER(HttpStatus.BAD_REQUEST,119,"일시적으로 로그인이 금지되었습니다."),
    BANNED_ID(HttpStatus.BAD_REQUEST,120,"금지된 단어가 포함된 아이디입니다."),
    BANNED_NICKNAME(HttpStatus.BAD_REQUEST,121,"금지된 닉네임 입니다."),

    INVALID_PAGINATION(HttpStatus.BAD_REQUEST,200, "올바르지 않은 페이지 정보를 전송하였습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, 201, "올바르지 않은 형식의 요청입니다."),


    NOT_EXIST_POST(HttpStatus.NOT_FOUND, 300, "존재하지 않는 게시글입니다."),
    NOT_EXIST_CATEGORY(HttpStatus.NOT_FOUND, 301, "존재하지 않는 카테고리입니다."),
    NOT_EXIST_ATTACHMENT(HttpStatus.NOT_FOUND, 302, "존재하지 않는 첨부파일입니다."),
    NOT_EXIST_COMMENT(HttpStatus.NOT_FOUND, 303, "존재하지 않는 댓글/답글입니다."),
    NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND, 304, "존재하지 않는 회원입니다."),
    NOT_ENROLLED_ROLE(HttpStatus.NOT_FOUND, 304, "등록되지 않은 권한 설정입니다."),
    NOT_EXIST_EXTENSION(HttpStatus.NOT_FOUND, 305, "사용가능하지 않은 확장자입니다."),

    DUPLICATED_REPORT(HttpStatus.BAD_REQUEST, 400, "이미 신고하였습니다."),
    ;

    ErrorCode(HttpStatus httpStatus, int code,String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
