package com.loc.auth_service.model.dto

import jakarta.validation.constraints.NotBlank

data class AuthRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,
    
    @field:NotBlank(message = "Password is required")
    val password: String
) 