package com.cozy.dto.response;

import lombok.Data;

@Data
public class StudentDetailsDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String universityName;
    private String phoneNumber;
}
