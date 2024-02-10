package com.cozy.entities;

import com.cozy.enumeration.TunisianCity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cozy.enumeration.PropertyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ref;
    private int numberOfRooms;
    private int bathrooms;
    private double areaInSquareMeters;
    private String location;
    private boolean furnished;
    private boolean air_conditioned;
    private boolean has_central_heating;
    private boolean parking;
    private String floor;
    private boolean garden;
    private String terms;
    private boolean garage;
    private boolean pool;

    private String propertyType;

    private double securityDeposit;
    private double rentPrice;
    private String description;
    @Enumerated(EnumType.STRING)
    private PropertyStatus status;


    @ManyToOne
    @JoinColumn(name = "homeowner_id")
    @JsonIgnore
    private Homeowner homeowner;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Image> images;


    @ManyToMany
    @JoinTable(
            name = "property_universities",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "university_id")
    )
    private List<University> universities;

    private String city;


}
