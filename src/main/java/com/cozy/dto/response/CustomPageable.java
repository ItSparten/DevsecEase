package com.cozy.dto.response;

import lombok.Data;

@Data
public class CustomPageable {
    private int pageSize;
    private int pageNumber;
    private long totalElements;
    private long totalPages;

    public CustomPageable(int pageSize, int pageNumber, long totalElements, long totalPages) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
