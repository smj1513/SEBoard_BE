package com.seproject.seboard.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class Author {
    public final static String ANONYMOUS_LOGIN_ID = "anonymous";
    private final Long authorId;
    private final String loginId;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return authorId.equals(author.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }

    public boolean isAnonymous() {
        return ANONYMOUS_LOGIN_ID.equals(loginId);
    }
}
