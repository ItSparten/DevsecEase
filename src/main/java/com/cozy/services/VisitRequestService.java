package com.cozy.services;

import com.cozy.dto.response.CustomPageResponse;
import com.cozy.dto.response.VisitRequestResponse;
import com.cozy.entities.VisitRequest;
import com.cozy.enumeration.VisitRequestStatus;

import java.time.LocalDate;

public interface VisitRequestService {
    VisitRequest createVisitRequest(Long studentId, Long propertyId, LocalDate visitDate,   String phone, String message);
    CustomPageResponse<VisitRequestResponse> getAllVisitRequestsByStudent(Long studentId, int page, int size);
    CustomPageResponse<VisitRequestResponse> getAllVisitRequests( int page, int size);

    CustomPageResponse<VisitRequestResponse> getAllVisitRequestsByAgent(Long agenttId, VisitRequestStatus status, int page, int size);


    VisitRequest changeStatus(Long visitId);

}
