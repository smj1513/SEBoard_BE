package com.seproject.account.jwt;

public class JWTProperties {
    public static final String TYPE = "type";
    public static final String ALGORITHM = "alg";
    public static final String HS256 = "HS256";
    public static final String AUTHORITIES = "authorities";

    //TODO : ENUM
    public static final String TEMPORAL_TOKEN = "temporalToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String LARGE_REFRESH_TOKEN = "largeRefreshToken";

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    public static final Integer ACCESS_TOKEN_EXPIRE = 30*60;
    public static final Integer REFRESH_TOKEN_EXPIRE = 6*60*60;
    public static final Integer LARGE_REFRESH_TOKEN_EXPIRE = 30*24*60*60;

}
