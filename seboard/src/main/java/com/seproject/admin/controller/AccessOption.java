package com.seproject.admin.controller;

import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;

public enum AccessOption {
    ALL("all"),
    OVER_USER("overUser"),
    OVER_KUMOH("overKumoh"),
    ONLY_ADMIN("onlyAdmin"),
    SELECT("select"),
    ;


    private String name;
    AccessOption(String name) {
        this.name = name;
    }


    public static AccessOption of(String name) {
        switch (name) {
            case "all" : return ALL;
            case "overUser" : return OVER_USER;
            case "overKumoh" : return OVER_KUMOH;
            case "onlyAdmin" : return ONLY_ADMIN;
            case "select" : return SELECT;
        }
        throw new CustomIllegalArgumentException(ErrorCode.CANNOT_FOUND_OPTION,null);
    }
}
