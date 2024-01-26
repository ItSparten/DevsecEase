package com.cozy.services.impl;

import com.cozy.dto.response.CustomPageResponse;
import com.cozy.dto.response.CustomPageable;
import com.cozy.entities.Property;
import com.cozy.entities.Rental;
import com.cozy.entities.Student;
import com.cozy.enumeration.PropertyStatus;
import com.cozy.exceptions.ConflictException;
import com.cozy.exceptions.NotFoundException;
import com.cozy.repositories.PropertyRepository;
import com.cozy.repositories.RentalRepository;
import com.cozy.repositories.StudentRepository;
import com.cozy.services.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final PropertyRepository propertyRepository;
    private final StudentRepository studentRepository;
    private final RentalRepository rentalRepository;

    @Override
    public Property rentProperty(Long propertyId, Long studentId, LocalDate startDate) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + propertyId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));

        // Assurez-vous que la maison n'est pas déjà louée
        if (property.getStatus() != PropertyStatus.RENTED) {
            Rental rental = new Rental();
            rental.setProperty(property);
            rental.setStudent(student);
            rental.setStartDate(startDate);

            rentalRepository.save(rental);

            property.setStatus(PropertyStatus.RENTED);
            return propertyRepository.save(property);
        } else {
            throw new ConflictException("This property is already rented.");
        }
    }

    @Override
    public CustomPageResponse<Rental> getAllRentalsByStudent(Long studentId, Pageable pageable) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));

        Page<Rental> rentals= rentalRepository.findByStudent(student, pageable);
        return  createCustomPageResponse(rentals);
    }

    @Override
    public CustomPageResponse<Rental> getAllRentals(Pageable pageable) {
        Page<Rental> rentals= rentalRepository.findAll(pageable);
        return createCustomPageResponse(rentals);
    }

    @Override
    public void cancelRentalByAgent(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new NotFoundException("Rental not found with id: " + rentalId));

        // Supprimez le rental
        rentalRepository.delete(rental);

        // Mettez à jour le statut de la propriété
        Property property = rental.getProperty();
        property.setStatus(PropertyStatus.PUBLISHED);
        propertyRepository.save(property);
    }

    private CustomPageResponse<Rental> createCustomPageResponse(Page<Rental> rentals) {
        CustomPageable customPageable = new CustomPageable(
                rentals.getPageable().getPageSize(),
                rentals.getPageable().getPageNumber(),
                rentals.getTotalElements(),
                rentals.getTotalPages()
        );
        return new CustomPageResponse<>(
                rentals.getContent(),
                customPageable
        );
    }
}
