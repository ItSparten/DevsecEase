package com.cozy.controllers;

import com.cozy.dto.request.RentPropertyRequest;
import com.cozy.dto.response.CustomPageResponse;
import com.cozy.dto.response.SuccessMessageResponse;
import com.cozy.entities.Rental;
import com.cozy.services.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping("/{propertyId}")
    public ResponseEntity<SuccessMessageResponse> rentProperty(@PathVariable Long propertyId, @RequestBody RentPropertyRequest request) {
        rentalService.rentProperty(propertyId, request.getStudentId(), request.getStartDate());
        return ResponseEntity.ok(new SuccessMessageResponse("Property rented successfully."));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<CustomPageResponse<Rental>> getAllRentalsByStudent(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(rentalService.getAllRentalsByStudent(studentId, pageRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<CustomPageResponse<Rental>> getAllRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(rentalService.getAllRentals(pageRequest));
    }


    @DeleteMapping("/{rentalId}/cancel")
    public ResponseEntity<SuccessMessageResponse> cancelRentalByAgent(@PathVariable Long rentalId) {
        rentalService.cancelRentalByAgent(rentalId);
        return ResponseEntity.ok(new SuccessMessageResponse("Rental canceled successfully by agent."));
    }


}
