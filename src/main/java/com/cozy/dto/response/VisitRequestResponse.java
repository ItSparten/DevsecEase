package com.cozy.dto.response;

import com.cozy.entities.Property;
import com.cozy.entities.Student;
import com.cozy.enumeration.VisitRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitRequestResponse {
    private Student student;
    private String phone;
    private Property property;
    private LocalDate visitDate;
    private  VisitRequestStatus status;
}
