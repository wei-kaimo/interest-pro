package com.interest.bbs.commom.model;

import lombok.Data;

@Data
public class PageWrapper {

    private int pageSize;

    private int start;

    public PageWrapper(int pageSize, int page) {
        this.pageSize = pageSize;
        this.start = page * pageSize;
    }

}
