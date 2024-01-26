package com.cozy.services;

import com.cozy.dto.response.CustomPageResponse;
import com.cozy.entities.Property;
import com.cozy.entities.Rental;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface RentalService {
    Property rentProperty(Long propertyId, Long studentId, LocalDate startDate);
    CustomPageResponse<Rental> getAllRentalsByStudent(Long studentId, Pageable pageable);
    CustomPageResponse<Rental> getAllRentals( Pageable pageable);
    void cancelRentalByAgent(Long rentalId);
}
