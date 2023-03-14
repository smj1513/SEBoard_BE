package com.seproject.seboard.domain.model.author;

import lombok.experimental.SuperBuilder;

import java.util.Objects;

@SuperBuilder
public abstract class Author {

    private Long authorId;
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
}
