package com.cozy.services;

import com.cozy.dto.response.AgentDetailsDTO;
import com.cozy.dto.response.CustomPageResponse;
import com.cozy.dto.response.HomeownerDetailsDTO;
import com.cozy.dto.response.StudentDetailsDTO;
import com.cozy.entities.Agent;
import com.cozy.entities.Homeowner;
import com.cozy.entities.Student;

public interface UserService {
    CustomPageResponse<Student> getAllStudents(int page, int size);
    CustomPageResponse<Agent> getAllAgent(int page, int size);
    CustomPageResponse<Homeowner> getAllHomeowner(int page, int size);
    void activateAccount(Long userId);
    void deactivateAccount(Long userId);

    AgentDetailsDTO getAgentById(Long agentId);
    HomeownerDetailsDTO getHomeownerById(Long homeownerId);
    StudentDetailsDTO getStudentById(Long studentId);
}
