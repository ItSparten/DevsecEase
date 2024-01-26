package com.cozy.services.impl;

import com.cozy.dto.response.*;
import com.cozy.entities.Agent;
import com.cozy.entities.Homeowner;
import com.cozy.entities.Student;
import com.cozy.entities.User;
import com.cozy.enumeration.PropertyStatus;
import com.cozy.exceptions.NotFoundException;
import com.cozy.repositories.*;
import com.cozy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final StudentRepository studentRepository;
    private final AgentRepository agentRepository;
    private final HomeownerRepository homeownerRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public CustomPageResponse<Student> getAllStudents(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Student> students= studentRepository.findAll(pageRequest);
        CustomPageable customPageable = new CustomPageable(
                students.getPageable().getPageSize(),
                students.getPageable().getPageNumber(),
                students.getTotalElements(),
                students.getTotalPages()
        );
        return new CustomPageResponse<>(
                students.getContent(),
                customPageable
        );
    }

    @Override
    public CustomPageResponse<Agent> getAllAgent(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Agent> agents= agentRepository.findAll(pageRequest);
        CustomPageable customPageable = new CustomPageable(
                agents.getPageable().getPageSize(),
                agents.getPageable().getPageNumber(),
                agents.getTotalElements(),
                agents.getTotalPages()
        );
        return new CustomPageResponse<>(
                agents.getContent(),
                customPageable
        );
    }

    @Override
    public CustomPageResponse<Homeowner> getAllHomeowner(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Homeowner> homeowners= homeownerRepository.findAll(pageRequest);
        CustomPageable customPageable = new CustomPageable(
                homeowners.getPageable().getPageSize(),
                homeowners.getPageable().getPageNumber(),
                homeowners.getTotalElements(),
                homeowners.getTotalPages()
        );
        return new CustomPageResponse<>(
                homeowners.getContent(),
                customPageable
        );
    }

    @Override
    public void activateAccount(Long userId) {
        logger.info("Trying to activate account for user with ID: : {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        user.setActive(true);
        userRepository.save(user);
        logger.info("Account activated successfully for user with ID: : {}", userId);
    }

    @Override
    public void deactivateAccount(Long userId) {
        logger.info("Trying to deactivate account for user with ID: : {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        user.setActive(false);
        userRepository.save(user);
        logger.info("Account deactivated successfully for user with ID: : {}", userId);
    }

    @Override
    public AgentDetailsDTO getAgentById(Long agentId) {
        Agent agent =  agentRepository.findById(agentId)
                .orElseThrow(() -> new NotFoundException("Agent not found with ID: " + agentId));

        long propertiesWithAgentStatus = propertyRepository.countPropertiesByStatusAndAgentId( PropertyStatus.WITH_AGENT, agentId);
        long propertiesPublished = propertyRepository.countPropertiesByStatusAndAgentId(PropertyStatus.PUBLISHED, agentId);
            AgentDetailsDTO responseDTO = new AgentDetailsDTO();
            responseDTO.setId(agent.getId());
            responseDTO.setFirstname(agent.getFirstname());
            responseDTO.setLastname(agent.getLastname());
            responseDTO.setEmail(agent.getEmail());
            responseDTO.setPhoneNumber(agent.getPhoneNumber());
            responseDTO.setPropertiesWithAgent(propertiesWithAgentStatus);
            responseDTO.setPropertiesPublished(propertiesPublished);

        return responseDTO;
    }

    @Override
    public HomeownerDetailsDTO getHomeownerById(Long homeownerId) {
        Homeowner homeowner =  homeownerRepository.findById(homeownerId)
                .orElseThrow(() -> new NotFoundException("homeowner not found with ID: " + homeownerId));
HomeownerDetailsDTO responseDTO = new HomeownerDetailsDTO();
      responseDTO.setId(homeowner.getId());
      responseDTO.setFirstname(homeowner.getFirstname());
      responseDTO.setLastname(homeowner.getLastname());
      responseDTO.setEmail(homeowner.getEmail());
      responseDTO.setAge(homeowner.getAge());
      responseDTO.setPhoneNumber(homeowner.getPhoneNumber());
      responseDTO.setNationalIdentityCard(homeowner.getNationalIdentityCard());
        return responseDTO;
    }

    @Override
    public StudentDetailsDTO getStudentById(Long studentId) {
        Student student =  studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Stduent not found with ID: " + studentId));
        StudentDetailsDTO responseDTO = new StudentDetailsDTO();
        responseDTO.setId(student.getId());
        responseDTO.setFirstname(student.getFirstname());
        responseDTO.setLastname(student.getLastname());
        responseDTO.setEmail(student.getEmail());
        responseDTO.setUniversityName(student.getUniversityName());
        responseDTO.setPhoneNumber(student.getPhoneNumber());
        return responseDTO;
    }

}
