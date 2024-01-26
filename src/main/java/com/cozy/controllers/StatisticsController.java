package com.cozy.controllers;

import com.cozy.dto.response.AgentStatisticsResponse;
import com.cozy.dto.response.AdminStatisticsResponse;

import com.cozy.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    // Statistiques globales pour l'administrateur
    @GetMapping("/total-statistics")
    public ResponseEntity<AdminStatisticsResponse> getTotalStatistics() {
        AdminStatisticsResponse totalStatistics = statisticsService.calculateTotalStatistics();
        return ResponseEntity.ok(totalStatistics);
    }

    // Statistiques pour un agent spécifique
    @GetMapping("/agent-statistics/{agentId}")
    public ResponseEntity<AgentStatisticsResponse> getAgentStatistics(@PathVariable Long agentId) {
        AgentStatisticsResponse agentStatistics = statisticsService.calculateAgentStatistics(agentId);
        return ResponseEntity.ok(agentStatistics);
    }



}

