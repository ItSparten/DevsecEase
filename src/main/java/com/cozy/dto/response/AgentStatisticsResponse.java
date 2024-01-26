package com.cozy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentStatisticsResponse {
    private Long totalPropertyRENTED;
    private Long totalPropertyWITH_AGENT;
    private Long totalPropertyPUBLISHED;
    private Long totalPropertyREJECTED;
    private Long totalPropertyAPPROVED;
    private Long totalVisitRequests;

}
