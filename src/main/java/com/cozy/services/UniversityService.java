package com.cozy.services;

import com.cozy.dto.request.UniversityRequest;
import com.cozy.entities.University;

import java.util.List;

public interface UniversityService {
    University createUniversity(UniversityRequest universityRequest);
    University getUniversityById(Long id);
    List<University> getAllUniversities();
    University updateUniversity(Long id, UniversityRequest universityRequest);
    void deleteUniversity(Long id);
}
