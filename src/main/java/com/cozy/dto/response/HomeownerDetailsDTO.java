package com.cozy.dto.response;

import lombok.Data;

@Data
public class HomeownerDetailsDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private int age;
    private String nationalIdentityCard;
}
