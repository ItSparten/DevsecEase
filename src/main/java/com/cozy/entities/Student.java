package com.cozy.entities;

import com.cozy.enumeration.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student extends User {
    private String universityName;
    private String phoneNumber;
    public Student(String firstname, String lastname, String email, String password,
                 Role role, String phoneNumber, String universityName) {
        super(firstname, lastname, email, password, role);
        this.phoneNumber = phoneNumber;
        this.universityName = universityName;

    }
}
