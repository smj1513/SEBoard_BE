package com.seproject.seboard.domain.repository;

import lombok.Getter;

@Getter
public class PagingInfo {
    private int curPage;
    private int perPage;
    private int offset;


    public PagingInfo(int curPage, int perPage) {
        this.curPage = curPage;
        this.perPage = perPage;
        this.offset = (curPage - 1) * perPage;
    }
}
