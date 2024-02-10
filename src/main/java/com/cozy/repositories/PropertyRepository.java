package com.cozy.repositories;

import com.cozy.entities.Agent;
import com.cozy.entities.Property;
import com.cozy.enumeration.PropertyStatus;
import com.cozy.enumeration.TunisianCity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PropertyRepository  extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
    Page<Property> findByHomeownerId(Long homeownerId, Pageable pageable);
    Page<Property> findByHomeownerIdAndStatus(Long homeownerId,PropertyStatus status, Pageable pageable);
    Page<Property> findByStatusOrderByIdDesc(PropertyStatus status, Pageable pageable);
    Page<Property> findByStatusAndCityOrderByIdDesc(PropertyStatus status, TunisianCity cityFilter, Pageable pageable);
    Page<Property>  findAllByAgentOrderByIdDesc(Agent agent, Pageable pageable);
    long countByStatus(PropertyStatus status);
    


    long countPropertiesByStatusAndAgentId(PropertyStatus status, Long agentId);
    List<Property> findTop5ByStatusOrderByIdDesc(PropertyStatus status);

    List<Property> findByStatus(PropertyStatus status);
}
