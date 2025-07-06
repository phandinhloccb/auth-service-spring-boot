package com.loc.auth_service.application.service

import com.loc.auth_service.application.port.RefreshTokenRepositoryPort
import com.loc.auth_service.application.port.UserRepositoryPort
import com.loc.authservice.model.RefreshTokenRequest
import org.springframework.stereotype.Service

@Service
class LogoutUserService(
    private val refreshTokenRepositoryPort: RefreshTokenRepositoryPort,
    private val userRepositoryPort: UserRepositoryPort
) {
    
    fun logout(refreshTokenRequest: RefreshTokenRequest) {
        refreshTokenRepositoryPort.deleteByToken(refreshTokenRequest.refreshToken)
    }
    
    fun logoutAll(username: String) {
        val user = userRepositoryPort.findByUsername(username)
            ?: throw RuntimeException("User not found")
        
        refreshTokenRepositoryPort.revokeAllByUserId(user.id)
    }
} 