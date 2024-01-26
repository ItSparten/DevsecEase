package com.cozy.services.impl;

import com.cozy.dto.request.UniversityRequest;
import com.cozy.entities.University;
import com.cozy.exceptions.NotFoundException;
import com.cozy.repositories.UniversityRepository;
import com.cozy.services.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;



    @Override
    public University createUniversity(UniversityRequest universityRequest) {
        var university = University.builder()
                .universityName(universityRequest.getUniversityName())
                .location(universityRequest.getLocation())
                .build();
        return universityRepository.save(university);
    }

    @Override
    public University getUniversityById(Long id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
    }

    @Override
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    @Override
    public University updateUniversity(Long id, UniversityRequest universityRequest) {
        University existingUniversity = getUniversityById(id);
        existingUniversity.setUniversityName(universityRequest.getUniversityName());
        existingUniversity.setLocation(universityRequest.getLocation());
        // Set other properties you want to update
        return universityRepository.save(existingUniversity);
    }

    @Override
    public void deleteUniversity(Long id) {
        University university = getUniversityById(id);
        universityRepository.delete(university);
    }
}

