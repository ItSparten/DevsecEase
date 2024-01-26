package com.cozy.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class CustomPageResponse<T> {
    private List<T> content;
    private CustomPageable pageable;

    public CustomPageResponse(List<T> content, CustomPageable pageable) {
        this.content = content;
        this.pageable = pageable;
    }


}
