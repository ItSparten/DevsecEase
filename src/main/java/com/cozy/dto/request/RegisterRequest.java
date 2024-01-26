package com.cozy.dto.request;

import com.cozy.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;

    private String phoneNumber;
    private int age;
    private String nationalIdentityCard;
    private String universityName;



}
