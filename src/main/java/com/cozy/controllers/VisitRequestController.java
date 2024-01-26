package com.cozy.controllers;

import com.cozy.dto.request.VisitRequestCreateRequest;
import com.cozy.dto.response.CustomPageResponse;
import com.cozy.dto.response.SuccessMessageResponse;
import com.cozy.dto.response.VisitRequestResponse;
import com.cozy.entities.VisitRequest;
import com.cozy.enumeration.VisitRequestStatus;
import com.cozy.services.VisitRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/visit-requests")
@RequiredArgsConstructor
public class VisitRequestController {

    private final VisitRequestService visitRequestService;



    @PostMapping("/create")
    public ResponseEntity<SuccessMessageResponse> createVisitRequest(@RequestBody VisitRequestCreateRequest request) {
        visitRequestService.createVisitRequest(request.getStudentId(), request.getPropertyId(), request.getVisitDate(), request.getPhone());
        return ResponseEntity.ok(new SuccessMessageResponse("Visit request created successfully."));
    }

    @PutMapping("/finshed/{id}")
    public ResponseEntity<VisitRequest> changeStatus(@PathVariable Long id) {
       VisitRequest visitRequest= visitRequestService.changeStatus(id);
        return ResponseEntity.ok(visitRequest);
    }

    @GetMapping("/by-student/{studentId}")
    public ResponseEntity<CustomPageResponse<VisitRequestResponse>> getAllVisitRequestsByStudent(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<VisitRequestResponse> customResponse = visitRequestService.getAllVisitRequestsByStudent(studentId, page, size);
        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/by-agent/{agentId}/{status}")
    public ResponseEntity<CustomPageResponse<VisitRequestResponse>> getAllVisitRequestsByAgent(
            @PathVariable Long agentId,
            @PathVariable VisitRequestStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<VisitRequestResponse> customResponse = visitRequestService.getAllVisitRequestsByAgent(agentId,status, page, size);
        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomPageResponse<VisitRequestResponse>> getAllVisitRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<VisitRequestResponse> customResponse = visitRequestService.getAllVisitRequests(page, size);
        return ResponseEntity.ok(customResponse);
    }
}

