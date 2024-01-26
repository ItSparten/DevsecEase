package com.cozy.repositories;

import com.cozy.entities.Homeowner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeownerRepository  extends JpaRepository<Homeowner, Long> {
}
