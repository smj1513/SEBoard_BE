package com.seproject.error.exception;

import org.springframework.security.access.AccessDeniedException;

public class IpAccessDeniedException extends AccessDeniedException {
    public IpAccessDeniedException(String msg) {
        super(msg);
    }
}
