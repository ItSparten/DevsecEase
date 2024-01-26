package com.cozy.dto.request;

import lombok.Data;

@Data
public class PropertyRequest {

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
    private Long homeownerId;
}
