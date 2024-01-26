package com.cozy.controllers;

import com.cozy.dto.response.*;
import com.cozy.entities.Agent;
import com.cozy.entities.Homeowner;
import com.cozy.entities.Student;
import com.cozy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/students")
    public ResponseEntity<CustomPageResponse<Student>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllStudents(page, size));
    }

    @GetMapping("/agents")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomPageResponse<Agent>> getAllAgents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllAgent(page, size));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<AgentDetailsDTO> getAgentById(@PathVariable Long agentId) {
            return ResponseEntity.ok(userService.getAgentById(agentId));
    }
    @GetMapping("/homeowner/{homeownerId}")
    public ResponseEntity<HomeownerDetailsDTO> getHomeownerById(@PathVariable Long homeownerId) {
        return ResponseEntity.ok(userService.getHomeownerById(homeownerId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentDetailsDTO> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(userService.getStudentById(studentId));
    }

    @GetMapping("/homeowners")
    public ResponseEntity<CustomPageResponse<Homeowner>> getAllHomeowners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllHomeowner(page, size));
    }

    @PostMapping("/activate/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessMessageResponse> activateAccount(@PathVariable Long userId) {
        userService.activateAccount(userId);
        return ResponseEntity.ok(new SuccessMessageResponse("Account activated successfully."));
    }

    @PostMapping("/deactivate/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessMessageResponse> deactivateAccount(@PathVariable Long userId) {
        userService.deactivateAccount(userId);
        return ResponseEntity.ok(new SuccessMessageResponse("Account deactivated successfully."));
    }

}
