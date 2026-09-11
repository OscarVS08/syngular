package com.vsolabs.syngular.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String roleName;
    private Boolean isActive;
}