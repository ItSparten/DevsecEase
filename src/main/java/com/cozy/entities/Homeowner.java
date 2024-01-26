package com.cozy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cozy.enumeration.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "homeowners")
public class Homeowner extends User{
    private String phoneNumber;
    private int age;
    private String nationalIdentityCard;
@JsonIgnore
    @OneToMany(mappedBy = "homeowner")
    private List<Property> properties;


    public Homeowner(String firstname, String lastname, String email, String password,
                     Role role, String phoneNumber, int age, String nationalIdentityCard) {
        super(firstname, lastname, email, password, role);
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.nationalIdentityCard = nationalIdentityCard;

    }
}
