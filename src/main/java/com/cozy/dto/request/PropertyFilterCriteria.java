package com.cozy.dto.request;
import lombok.Data;
@Data
public class PropertyFilterCriteria {
    private String location;
    private int minRooms;
    private int maxRooms;
    private String propertyType;
    private double minRentPrice;
    private double maxRentPrice;
    private Long universityId;
    // Add other filtering fields as needed, such as property features, etc.
}

