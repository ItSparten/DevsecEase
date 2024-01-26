package com.cozy.dto.request;

import lombok.Data;

@Data
public class ContactUsRequest {
    private String name;
    private String email;
    private String message;
}
