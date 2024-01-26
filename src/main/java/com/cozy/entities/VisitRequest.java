package com.cozy.entities;

import com.cozy.enumeration.VisitRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visit_requests")
public class VisitRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String phone;

    private LocalDate visitDate;

    @Enumerated(EnumType.STRING)
    private VisitRequestStatus status;
}
