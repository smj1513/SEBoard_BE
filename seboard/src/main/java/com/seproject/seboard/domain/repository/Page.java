package com.seproject.seboard.domain.repository;

import lombok.Getter;

import java.util.List;


@Getter
public class Page<E> {
    private final int totalSize;
    private final int curPage;
    private final int lastPage;
    private final int perPage;
    private final List<E> data;

    public Page(int curPage, int perPage, int totalSize, List<E> data) {
        this.curPage = curPage;
        this.perPage = perPage;
        this.totalSize = totalSize;
        this.data = data;
        this.lastPage = totalSize / perPage;
    }

    public List<E> getData(){
        return List.copyOf(data);
    }
}
