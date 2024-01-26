package com.cozy.dto.response;

import lombok.Data;

@Data
public class AgentDetailsDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private long propertiesWithAgent;
    private long propertiesPublished;
}
