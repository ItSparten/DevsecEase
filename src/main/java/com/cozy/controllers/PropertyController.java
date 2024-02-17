package com.cozy.controllers;

import com.cozy.dto.request.PropertyFilterCrit;
import com.cozy.dto.request.PropertyRequest;
import com.cozy.dto.response.CustomPageResponse;
import com.cozy.entities.Property;
import com.cozy.enumeration.PropertyStatus;
import com.cozy.enumeration.TunisianCity;
import com.cozy.services.PropertyService;
import com.cozy.services.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PropertyController {
    private final PropertyService propertyService;
    private final EmailService emailService;

    @PostMapping("/add")
    public ResponseEntity<Property> addProperty(@ModelAttribute PropertyRequest propertyRequest,
                                                @RequestParam("images") List<MultipartFile> images) throws MessagingException, IOException {
        Property addedProperty = propertyService.addProperty(propertyRequest, images);
        emailService.sendNewPubEmail(addedProperty);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProperty);
    }

    @GetMapping("/by-homeowner/{homeownerId}")
    public ResponseEntity<CustomPageResponse<Property>> getPropertiesByHomeowner(
            @PathVariable Long homeownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Property> properties = propertyService.getPropertiesByHomeowner(homeownerId, page, size);
        return ResponseEntity.ok(properties);
    }
    @GetMapping("/by-homeowner/{homeownerId}/{status}")
    public ResponseEntity<CustomPageResponse<Property>> getPropertiesByHomeownerByStatus(
            @PathVariable Long homeownerId,
            @PathVariable PropertyStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Property> properties = propertyService.getPropertiesByHomeownerByStatus(homeownerId, status, page, size);
        return ResponseEntity.ok(properties);
    }

 /*   @GetMapping("/by-status-and-city/{status}")
    public ResponseEntity<CustomPageResponse<Property>> getPropertiesByStatus(
            @PathVariable PropertyStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TunisianCity cityFilter) {
        CustomPageResponse<Property> properties;
        if (cityFilter != null) {
            // Si cityFilter est fourni, utilisez-le pour filtrer les propriétés par ville
            properties = propertyService.getPropertiesByStatusAndCity(status,cityFilter, page, size );
        } else {
            // Sinon, obtenez toutes les propriétés publiées sans filtre de ville
            properties = propertyService.getPropertiesByStatus(status, page, size);
        }
        return ResponseEntity.ok(properties);
    }*/

    @GetMapping("/by-status/{status}")
    public ResponseEntity<CustomPageResponse<Property>> getPropertiesByStatus(
            @PathVariable PropertyStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Property> properties = propertyService.getPropertiesByStatus(status, page, size);
        return ResponseEntity.ok(properties);
    }
    @GetMapping("/by-status-and-university/{status}/{universityId}")
    public ResponseEntity<CustomPageResponse<Property>> getPropertiesByStatusAndUniversity(
            @PathVariable PropertyStatus status,
            @PathVariable(required = false) Long universityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
            CustomPageResponse<Property> properties = propertyService.getPropertiesByStatusAndUniversity(status, universityId, page, size);
            return ResponseEntity.ok(properties);
    }
    @GetMapping("/by-status-and-city/{status}/{city}")
    public ResponseEntity<CustomPageResponse<Property>> getPropertiesByStatusAndCity(
            @PathVariable PropertyStatus status,
            @PathVariable(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Property> properties = propertyService.getPropertiesByStatusAndCity(status, city, page, size);
        return ResponseEntity.ok(properties);
    }


    @PostMapping("/{propertyId}/assign-agent/{agentId}")
    public ResponseEntity<Property> assignPropertyToAgent(@PathVariable Long propertyId, @PathVariable Long agentId) {
        Property assignedProperty = propertyService.assignPropertyToAgent(propertyId, agentId);
        return ResponseEntity.ok(assignedProperty);
    }

    @GetMapping("/by-agent/{agentId}")
    public ResponseEntity<CustomPageResponse<Property>> getPropertiesByAgent(
            @PathVariable Long agentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Property> properties = propertyService.getPropertiesByAgent(agentId, page, size);
        return ResponseEntity.ok(properties);
    }

    @PostMapping("/{propertyId}/assign-universities")
    public ResponseEntity<Property> assignPropertyToUniversities(@PathVariable Long propertyId, @RequestBody List<Long> universityIds) {
        Property assignedProperty = propertyService.assignPropertyToUniversities(propertyId, universityIds);
        return ResponseEntity.ok(assignedProperty);
    }


    @DeleteMapping("/{propertyId}/remove-universities")
    public ResponseEntity<Property> removeUniversitiesFromProperty(@PathVariable Long propertyId, @RequestBody List<Long> universityIds) {
        Property updatedProperty = propertyService.removeUniversitiesFromProperty(propertyId, universityIds);
        return ResponseEntity.ok(updatedProperty);
    }

    @PutMapping("/publish/{propertyId}")
    public ResponseEntity<Property> publishProperty(@PathVariable Long propertyId) throws MessagingException {
        Property publishedProperty = propertyService.publishProperty(propertyId);
        sendEmailToHomeowner(publishedProperty);
        return ResponseEntity.status(HttpStatus.CREATED).body(publishedProperty);
    }

    private void sendEmailToHomeowner(Property publishedProperty) throws MessagingException {
        String subject = "Votre propriété a été publiée avec succès";
        String message = "Votre propriété a été publiée avec succès. Félicitations!";
        String homeownerEmail = publishedProperty.getHomeowner().getEmail();

        // Appeler la méthode d'envoi d'e-mail de votre service EmailService
        emailService.sendEmail(homeownerEmail, subject, message);
    }


    @GetMapping("/filter")
    public ResponseEntity<CustomPageResponse<Property>>  filterProperties(
            @ModelAttribute PropertyFilterCrit filterCriteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        CustomPageResponse<Property> customResponse = propertyService.filterProperties(filterCriteria, pageable);
        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/byid/{id}")
    public ResponseEntity<Property> getPropertyById(
            @PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @GetMapping("/latestPublished")
    public List<Property> getLatestPublishedProperties() {
        return propertyService.getLatestPublishedProperties();
    }
}
