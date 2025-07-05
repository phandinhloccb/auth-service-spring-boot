package com.loc.auth_service.application.service

import com.loc.auth_service.application.port.UserRepositoryPort
import com.loc.auth_service.application.port.JwtTokenPort
import com.loc.auth_service.domain.model.User
import org.springframework.stereotype.Service

@Service
class ValidateTokenService(
    private val userRepositoryPort: UserRepositoryPort,
    private val jwtTokenPort: JwtTokenPort
) {
    
    fun validateToken(token: String): User {
        val username = jwtTokenPort.extractUsername(token)
        val user = userRepositoryPort.findByUsername(username)
            ?: throw RuntimeException("User not found")
        
        if (!jwtTokenPort.isTokenValid(token, user)) {
            throw RuntimeException("Invalid token")
        }
        
        return user
    }
} 