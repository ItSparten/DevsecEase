package com.cozy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminStatisticsResponse {
    private Long totalStudents;
    private Long totalHomeowners;
    private Long totalAgents;
    private Long totalPropertyRENTED;
    private Long totalPropertyWITH_AGENT;
    private Long totalPropertyPUBLISHED;
    private Long totalPropertyPENDING_VALIDATION;
    private Long totalPropertyREJECTED;
    private Long totalPropertyAPPROVED;
    private Long totalVisitRequests;
}
