package com.cozy.dto.request;

import com.cozy.enumeration.TunisianCity;
import lombok.Data;

@Data
public class PropertyFilterCrit {
    private int nbRooms;
    private String propertyType;
    private double rentPrice;
    private Long universityId;
    private String city; //  city property
}
