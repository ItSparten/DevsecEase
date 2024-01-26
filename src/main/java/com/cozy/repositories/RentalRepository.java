package com.cozy.repositories;

import com.cozy.entities.Rental;
import com.cozy.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Page<Rental> findByStudent(Student student, Pageable pageable);
}
