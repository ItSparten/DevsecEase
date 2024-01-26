package com.cozy.entities;

import com.cozy.enumeration.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agents")
public class Agent extends User {
    private String phoneNumber;

    public Agent(String firstname, String lastname, String email, String password,
                     Role role, String phoneNumber) {
        super(firstname, lastname, email, password, role);
        this.phoneNumber = phoneNumber;

    }

}
