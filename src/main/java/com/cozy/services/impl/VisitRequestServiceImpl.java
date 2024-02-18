package com.cozy.services.impl;

import com.cozy.dto.response.CustomPageResponse;
import com.cozy.dto.response.CustomPageable;
import com.cozy.dto.response.VisitRequestResponse;
import com.cozy.entities.Property;
import com.cozy.entities.Student;
import com.cozy.entities.VisitRequest;
import com.cozy.enumeration.VisitRequestStatus;
import com.cozy.exceptions.ConflictException;
import com.cozy.exceptions.NotFoundException;
import com.cozy.repositories.PropertyRepository;
import com.cozy.repositories.StudentRepository;
import com.cozy.repositories.VisitRequestRepository;
import com.cozy.services.VisitRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitRequestServiceImpl implements VisitRequestService {
    private final VisitRequestRepository visitRequestRepository;
    private final StudentRepository studentRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public VisitRequest createVisitRequest(Long studentId, Long propertyId, LocalDate visitDate,  String phone) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + propertyId));
        // Vérifiez si l'étudiant a déjà une demande de visite pour la même maison
        visitRequestRepository.findByStudentAndProperty(student, property)
                .ifPresent(existingRequest -> {
            throw new ConflictException("You already have a visit request for this property on " + existingRequest.getVisitDate());
        });
        VisitRequest visitRequest = new VisitRequest();
        visitRequest.setStudent(student);
        visitRequest.setProperty(property);
        visitRequest.setVisitDate(visitDate);
        visitRequest.setPhone(phone);
        visitRequest.setStatus(VisitRequestStatus.INPROGRESS);
        return visitRequestRepository.save(visitRequest);
    }

    @Override
    public CustomPageResponse<VisitRequestResponse> getAllVisitRequestsByStudent(Long studentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VisitRequest> visitRequestsPage = visitRequestRepository.findByStudentId(studentId, pageable);
        CustomPageable customPageable = new CustomPageable(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                visitRequestsPage.getTotalElements(),
                visitRequestsPage.getTotalPages()
        );

        List<VisitRequestResponse> visitRequestResponses = visitRequestsPage.getContent()
                .stream()
                .map(this::mapToVisitRequestResponse)
                .collect(Collectors.toList());

        return new CustomPageResponse<>(visitRequestResponses, customPageable);

    }

    @Override
    public CustomPageResponse<VisitRequestResponse> getAllVisitRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VisitRequest> visitRequestsPage = visitRequestRepository.findAll(pageable);
        CustomPageable customPageable = new CustomPageable(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                visitRequestsPage.getTotalElements(),
                visitRequestsPage.getTotalPages()
        );

        List<VisitRequestResponse> visitRequestResponses = visitRequestsPage.getContent()
                .stream()
                .map(this::mapToVisitRequestResponse)
                .collect(Collectors.toList());

        return new CustomPageResponse<>(visitRequestResponses, customPageable);
    }

    @Override
    public CustomPageResponse<VisitRequestResponse> getAllVisitRequestsByAgent(Long agenttId,VisitRequestStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VisitRequest> visitRequestsPage = visitRequestRepository.findByProperty_Agent_IdAndStatus(agenttId,status, pageable);
        CustomPageable customPageable = new CustomPageable(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                visitRequestsPage.getTotalElements(),
                visitRequestsPage.getTotalPages()
        );

        List<VisitRequestResponse> visitRequestResponses = visitRequestsPage.getContent()
                .stream()
                .map(this::mapToVisitRequestResponse)
                .collect(Collectors.toList());

        return new CustomPageResponse<>(visitRequestResponses, customPageable);
    }

    @Override
    public VisitRequest changeStatus(Long visitId) {
        VisitRequest visitRequest = visitRequestRepository.findById(visitId).get();
        visitRequest.setStatus(VisitRequestStatus.FINISHED);
        return visitRequestRepository.save(visitRequest);
    }

    private VisitRequestResponse mapToVisitRequestResponse(VisitRequest visitRequest) {
        VisitRequestResponse response = new VisitRequestResponse();
        response.setStudent(visitRequest.getStudent());
        response.setProperty(visitRequest.getProperty());
        response.setVisitDate(visitRequest.getVisitDate());
        response.setPhone(visitRequest.getPhone());
        response.setStatus(visitRequest.getStatus());
        response.setMessage(visitRequest.getMessage());
        response.setId(visitRequest.getId());
        return response;
    }
}
