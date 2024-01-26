package com.cozy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitRequestCreateRequest {
    private Long studentId;
    private String phone;
    private Long propertyId;
    private LocalDate visitDate;
}
