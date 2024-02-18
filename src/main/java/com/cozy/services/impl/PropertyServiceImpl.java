package com.cozy.services.impl;

import com.cozy.dto.request.PropertyFilterCrit;
import com.cozy.dto.request.PropertyFilterCriteria;
import com.cozy.dto.request.PropertyRequest;
import com.cozy.dto.response.CustomPageResponse;
import com.cozy.dto.response.CustomPageable;
import com.cozy.entities.*;
import com.cozy.enumeration.PropertyStatus;
import com.cozy.enumeration.TunisianCity;
import com.cozy.exceptions.BadRequestException;
import com.cozy.exceptions.ImageLimitExceededException;
import com.cozy.exceptions.NotFoundException;
import com.cozy.repositories.AgentRepository;
import com.cozy.repositories.HomeownerRepository;
import com.cozy.repositories.PropertyRepository;
import com.cozy.repositories.UniversityRepository;
import com.cozy.services.PropertyService;
import com.cozy.specifications.PropertySpecifications;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final HomeownerRepository homeownerRepository;
    private final AgentRepository agentRepository;
    private final UniversityRepository universityRepository;
    @Value("${application.image.remoteUrl}")
    private String baseUrl;

    @Value("${application.image.path.remote}")
    private String imagePath;
    @Override
    public Property addProperty(PropertyRequest propertyRequest,  List<MultipartFile> images) {
        // Add homeowner to Property
        Homeowner homeowner = homeownerRepository.findById(propertyRequest.getHomeownerId())
                .orElseThrow(() -> new NotFoundException("Homeowner not found with id: " + propertyRequest.getHomeownerId()));
        if (images.size() > 5) {
            throw new ImageLimitExceededException("You can only upload up to 5 images per property.");
        }
        Property property = new Property();
        property.setRef(generateUniqueReference());
        property.setNumberOfRooms(propertyRequest.getNumberOfRooms());
        property.setBathrooms(propertyRequest.getBathrooms());
        property.setLocation(propertyRequest.getLocation());
        property.setFurnished(propertyRequest.isFurnished());
        property.setAir_conditioned(propertyRequest.isAir_conditioned());
        property.setHas_central_heating(propertyRequest.isHas_central_heating());
        property.setFloor(propertyRequest.getFloor());
        property.setGarden(propertyRequest.isGarden());
        property.setTerms(propertyRequest.getTerms());
        property.setGarage(propertyRequest.isGarage());
        property.setPool(propertyRequest.isPool());
        property.setPropertyType(propertyRequest.getPropertyType());
        property.setSecurityDeposit(propertyRequest.getSecurityDeposit());
        property.setRentPrice(propertyRequest.getRentPrice());
        property.setDescription(propertyRequest.getDescription());
        property.setCity(propertyRequest.getCity());
        property.setStatus(PropertyStatus.PENDING_VALIDATION);


        property.setHomeowner(homeowner);


        List<Image> imagess = new ArrayList<>();
       // String baseUrl = "http://141.94.205.169:8095/api/v1/images/"; // Votre base URL
        // Enregistrez les images et associez-les à la propriété
        for (MultipartFile image : images) {
            Image imageEntity = new Image();
            imageEntity.setProperty(property);
            imageEntity.setImagePath(saveImage(image, property.getRef()));
            String imagePath = baseUrl + imageEntity.getImagePath(); // Construisez l'URL complet
            imageEntity.setImagePath(imagePath);
            imagess.add(imageEntity);
        }
        property.setImages(imagess);

        return propertyRepository.save(property);
    }

    private String saveImage(MultipartFile image, String ref) {
        String imageName =ref + "_" + UUID.randomUUID()+ '.'+image.getContentType().substring(6);
        imagePath = "/home/ubuntu/images/" + imageName;
        try {
            Files.copy(image.getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
        }
        return imageName;
    }

    @Override
    public CustomPageResponse<Property> getPropertiesByHomeowner(Long homeownerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> properties= propertyRepository.findByHomeownerId(homeownerId, pageRequest);
        return createCustomPageResponse(properties);
    }

    @Override
    public CustomPageResponse<Property> getPropertiesByHomeownerByStatus(Long homeownerId, PropertyStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> properties= propertyRepository.findByHomeownerIdAndStatus(homeownerId, status, pageRequest);
        return createCustomPageResponse(properties);
    }

    @Override
    public CustomPageResponse<Property> getPropertiesByStatus(PropertyStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> properties=   propertyRepository.findByStatusOrderByIdDesc(status, pageRequest);
        return createCustomPageResponse(properties);
    }

    @Override
    public CustomPageResponse<Property> getPropertiesByStatusAndUniversity(PropertyStatus status, Long universityId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> properties;
        List<Property> filteredProperties = propertyRepository.findByStatus(status);
        filteredProperties.removeIf(property -> !property.getUniversities().stream()
                .anyMatch(university -> university.getId().equals(universityId)));
        properties = new PageImpl<>(filteredProperties, pageRequest, filteredProperties.size());
        return createCustomPageResponse(properties);
    }

    @Override
    public CustomPageResponse<Property> getPropertiesByStatusAndCity(PropertyStatus status, String city, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> properties= propertyRepository.findByStatusAndCityOrderByIdDesc(status, city, pageRequest);
        return createCustomPageResponse(properties);
    }

    @Override
    public CustomPageResponse<Property> getPropertiesByStatusAndCity(PropertyStatus status,TunisianCity cityFilter, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> properties=   propertyRepository.findByStatusAndCityOrderByIdDesc(status,cityFilter, pageRequest);
        return createCustomPageResponse(properties);
    }



    @Override
    public Property assignPropertyToAgent(Long propertyId, Long agentId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + propertyId));

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new NotFoundException("Agent not found with id: " + agentId));

        property.setAgent(agent);
        property.setStatus(PropertyStatus.WITH_AGENT);
        return propertyRepository.save(property);
    }

    @Override
    public CustomPageResponse<Property> getPropertiesByAgent(Long agentId, int page, int size) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new NotFoundException("Agent not found with id: " + agentId));
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> properties= propertyRepository.findAllByAgentOrderByIdDesc(agent, pageRequest);
        return createCustomPageResponse(properties);
    }

    @Override
    public Property assignPropertyToUniversities(Long propertyId, List<Long> universityIds) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + propertyId));

        List<University> universities = universityRepository.findAllById(universityIds);
        property.setUniversities(universities);

        return propertyRepository.save(property);
    }

    @Override
    public Property removeUniversitiesFromProperty(Long propertyId, List<Long> universityIds) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + propertyId));

        List<University> universitiesToRemove = universityRepository.findAllById(universityIds);
        property.getUniversities().removeAll(universitiesToRemove);

        return propertyRepository.save(property);
    }

    @Override
    public Property publishProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + propertyId));
        if (property.getStatus() == PropertyStatus.PUBLISHED) {
            throw new BadRequestException("La propriété est déjà publiée.");
        }
        property.setStatus(PropertyStatus.PUBLISHED);


        return propertyRepository.save(property);
    }



    @Override
    public CustomPageResponse<Property> filterProperties(PropertyFilterCrit filterCriteria, Pageable pageable) {
        Specification<Property> spec = buildPropertySpecification2(filterCriteria);
        Page<Property> filteredProperties = propertyRepository.findAll(spec, pageable);
        return createCustomPageResponse(filteredProperties);
    }

    @Override
    public Property getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + id));
        return property;
    }

    @Override
    public List<Property> getLatestPublishedProperties() {
        return propertyRepository.findTop5ByStatusOrderByIdDesc(PropertyStatus.PUBLISHED);
    }

    private Specification<Property> buildPropertySpecification2(PropertyFilterCrit filterCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Add PUBLISHED status to all cases
            predicates.add(PropertySpecifications.statusIs(PropertyStatus.PUBLISHED).toPredicate(root, query, criteriaBuilder));


            if (filterCriteria.getNbRooms() > 0 ) {
                predicates.add(PropertySpecifications.roomsEqual(filterCriteria.getNbRooms()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getPropertyType() != null) {
                predicates.add(PropertySpecifications.propertyTypeEqual(filterCriteria.getPropertyType()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getRentPrice() > 0) {
                predicates.add(PropertySpecifications.rentPriceEqual(filterCriteria.getRentPrice()).toPredicate(root, query, criteriaBuilder));
            }


            if (filterCriteria.getUniversityId() != null) {
                predicates.add(PropertySpecifications.universityIs(filterCriteria.getUniversityId()).toPredicate(root, query, criteriaBuilder));
            }
            if (StringUtils.isNotBlank(filterCriteria.getCity())) {
                predicates.add(PropertySpecifications.cityIsLike(filterCriteria.getCity()).toPredicate(root, query, criteriaBuilder));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    private Specification<Property> buildPropertySpecification(PropertyFilterCriteria filterCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterCriteria.getLocation() != null) {
                predicates.add(PropertySpecifications.locationIsLike(filterCriteria.getLocation()).toPredicate(root, query, criteriaBuilder));
            }

            // 1er Critère: minRooms et maxRooms sont spécifiés
            if (filterCriteria.getMinRooms() > 0 && filterCriteria.getMaxRooms() > 0) {
                predicates.add(PropertySpecifications.roomsBetween(filterCriteria.getMinRooms(), filterCriteria.getMaxRooms()).toPredicate(root, query, criteriaBuilder));
            }

            // 2eme Critère: minRooms spécifié, maxRooms non spécifié
            if (filterCriteria.getMinRooms() > 0 &&  filterCriteria.getMaxRooms() == 0) {
                predicates.add(PropertySpecifications.roomsGreaterThanOrEqual(filterCriteria.getMinRooms()).toPredicate(root, query, criteriaBuilder));
            }

            // 3eme Critère: maxRooms spécifié, minRooms non spécifié
            if (filterCriteria.getMaxRooms() > 0 &&  filterCriteria.getMinRooms() == 0) {
                predicates.add(PropertySpecifications.roomsLessThanOrEqual(filterCriteria.getMaxRooms()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getPropertyType() != null) {
                predicates.add(PropertySpecifications.propertyTypeIs(filterCriteria.getPropertyType()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getMinRentPrice() >= 0) {
                predicates.add(PropertySpecifications.rentPriceGreaterThanOrEqual(filterCriteria.getMinRentPrice()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getMaxRentPrice() > 0) {
                predicates.add(PropertySpecifications.rentPriceLessThanOrEqual(filterCriteria.getMaxRentPrice()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getUniversityId() != null) {
                predicates.add(PropertySpecifications.universityIs(filterCriteria.getUniversityId()).toPredicate(root, query, criteriaBuilder));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private CustomPageResponse<Property> createCustomPageResponse(Page<Property> properties) {
        CustomPageable customPageable = new CustomPageable(
                properties.getPageable().getPageSize(),
                properties.getPageable().getPageNumber(),
                properties.getTotalElements(),
                properties.getTotalPages()
        );
        return new CustomPageResponse<>(
                properties.getContent(),
                customPageable
        );
    }


    private String generateUniqueReference() {
        int refLength = 10;
        StringBuilder refBuilder = new StringBuilder(refLength);

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        for (int i = 0; i < refLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            refBuilder.append(characters.charAt(randomIndex));
        }

        return refBuilder.toString();
    }
}
