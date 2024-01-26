package com.cozy.services.impl;

import com.cozy.dto.response.AgentStatisticsResponse;
import com.cozy.dto.response.AdminStatisticsResponse;
import com.cozy.enumeration.PropertyStatus;
import com.cozy.enumeration.Role;
import com.cozy.exceptions.NotFoundException;
import com.cozy.repositories.*;
import com.cozy.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final VisitRequestRepository visitRequestRepository;

    @Override
    public AdminStatisticsResponse calculateTotalStatistics() {
        long totalStudents = userRepository.countByRole(Role.STUDENT);
        long totalHomeowners = userRepository.countByRole(Role.HOMEOWNER);
        long totalAgents = userRepository.countByRole(Role.AGENT);

        long totalPropertyRENTED = propertyRepository.countByStatus(PropertyStatus.RENTED);
        long totalPropertyWITH_AGENT = propertyRepository.countByStatus(PropertyStatus.WITH_AGENT);
        long totalPropertyPUBLISHED = propertyRepository.countByStatus(PropertyStatus.PUBLISHED);
        long totalPropertyPENDING_VALIDATION = propertyRepository.countByStatus(PropertyStatus.PENDING_VALIDATION);
        long totalPropertyREJECTED = propertyRepository.countByStatus(PropertyStatus.REJECTED);
        long totalPropertyAPPROVED = propertyRepository.countByStatus(PropertyStatus.APPROVED);

        long totalVisitRequests = visitRequestRepository.countByStudentIdIsNotNull();



        return new AdminStatisticsResponse(
                totalStudents,
                totalHomeowners,
                totalAgents,
                totalPropertyRENTED,
                totalPropertyWITH_AGENT,
                totalPropertyPUBLISHED,
                totalPropertyPENDING_VALIDATION,
                totalPropertyREJECTED,
                totalPropertyAPPROVED,
                totalVisitRequests
        );
    }

    @Override
    public AgentStatisticsResponse calculateAgentStatistics(Long agentId) {
        // Vérifiez si l'agent avec l'ID donné existe
        userRepository.findById(agentId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + agentId));

        long totalPropertyRENTED = propertyRepository.countPropertiesByStatusAndAgentId(PropertyStatus.RENTED, agentId);
        long totalPropertyWITH_AGENT = propertyRepository.countPropertiesByStatusAndAgentId(PropertyStatus.WITH_AGENT, agentId);
        long totalPropertyPUBLISHED = propertyRepository.countPropertiesByStatusAndAgentId(PropertyStatus.PUBLISHED, agentId);
        long totalPropertyREJECTED = propertyRepository.countPropertiesByStatusAndAgentId(PropertyStatus.REJECTED, agentId);
        long totalPropertyAPPROVED = propertyRepository.countPropertiesByStatusAndAgentId(PropertyStatus.APPROVED, agentId);

        long totalVisitRequests = visitRequestRepository.countVisitRequestByProperty_AgentId(agentId);


        return new AgentStatisticsResponse(
                totalPropertyRENTED,
                totalPropertyWITH_AGENT,
                totalPropertyPUBLISHED,
                totalPropertyREJECTED,
                totalPropertyAPPROVED,
                totalVisitRequests
        );
    }


}
