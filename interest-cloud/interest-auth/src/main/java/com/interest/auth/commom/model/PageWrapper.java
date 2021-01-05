package com.interest.auth.commom.model;

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
