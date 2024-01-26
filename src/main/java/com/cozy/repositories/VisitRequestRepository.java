package com.cozy.repositories;

import com.cozy.entities.Property;
import com.cozy.entities.Student;
import com.cozy.entities.VisitRequest;
import com.cozy.enumeration.VisitRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitRequestRepository extends JpaRepository<VisitRequest, Long> {
    Optional<VisitRequest> findByStudentAndProperty(Student student, Property property);
    Page<VisitRequest> findByStudentId(Long studentId, Pageable pageable);
    Page<VisitRequest> findByProperty_Agent_Id(Long agenttId, Pageable pageable);
    Page<VisitRequest> findByProperty_Agent_IdAndStatus(Long agenttId, VisitRequestStatus status, Pageable pageable);
    long countByStudentIdIsNotNull();
    long countVisitRequestByProperty_AgentId(Long agentId);

}
