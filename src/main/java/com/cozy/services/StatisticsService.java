package com.cozy.services;

import com.cozy.dto.response.AgentStatisticsResponse;
import com.cozy.dto.response.AdminStatisticsResponse;

public interface StatisticsService {


    AdminStatisticsResponse calculateTotalStatistics();

    AgentStatisticsResponse calculateAgentStatistics(Long agentId);


}
