package com.cozy.services;

import com.cozy.dto.request.PropertyFilterCrit;
import com.cozy.dto.request.PropertyRequest;
import com.cozy.dto.response.CustomPageResponse;
import com.cozy.entities.Property;
import com.cozy.enumeration.PropertyStatus;
import com.cozy.enumeration.TunisianCity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface PropertyService {
    Property addProperty(PropertyRequest propertyRequest,  List<MultipartFile> images);
    CustomPageResponse<Property> getPropertiesByHomeowner(Long homeownerId, int page, int size);
    CustomPageResponse<Property> getPropertiesByHomeownerByStatus(Long homeownerId, PropertyStatus status, int page, int size);
    CustomPageResponse<Property> getPropertiesByStatus(PropertyStatus status, int page, int size);
    CustomPageResponse<Property> getPropertiesByStatusAndCity(PropertyStatus status, TunisianCity cityFilter, int page, int size);
    CustomPageResponse<Property> getPropertiesByStatusAndUniversity(PropertyStatus status, Long universityId, int page, int size);
    Property assignPropertyToAgent(Long propertyId, Long agentId);
    CustomPageResponse<Property> getPropertiesByAgent(Long agentId, int page, int size);
    Property assignPropertyToUniversities(Long propertyId, List<Long> universityIds);
    
    Property removeUniversitiesFromProperty(Long propertyId, List<Long> universityIds);
    Property publishProperty(Long propertyId);
    CustomPageResponse<Property> filterProperties(PropertyFilterCrit filterCriteria, Pageable pageable);

    Property getPropertyById(Long id);
    List<Property> getLatestPublishedProperties();
}
