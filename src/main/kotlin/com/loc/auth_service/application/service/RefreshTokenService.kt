package com.loc.auth_service.application.service

import com.loc.auth_service.application.port.RefreshTokenRepositoryPort
import com.loc.auth_service.application.port.UserRepositoryPort
import com.loc.auth_service.application.port.JwtTokenPort
import com.loc.auth_service.domain.model.RefreshToken
import com.loc.authservice.model.AuthResponse
import com.loc.authservice.model.RefreshTokenRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RefreshTokenService(
    private val refreshTokenRepositoryPort: RefreshTokenRepositoryPort,
    private val userRepositoryPort: UserRepositoryPort,
    private val jwtTokenPort: JwtTokenPort
) {
    
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): AuthResponse {
        val refreshToken = refreshTokenRepositoryPort.findByToken(refreshTokenRequest.refreshToken)
            ?: throw RuntimeException("Refresh token not found")
        
        if (refreshToken.isExpired()) {
            refreshTokenRepositoryPort.deleteByToken(refreshTokenRequest.refreshToken)
            throw RuntimeException("Refresh token is expired")
        }
        
        val user = userRepositoryPort.findById(refreshToken.userId)
            ?: throw RuntimeException("User not found")
        
        val newAccessToken = jwtTokenPort.generateAccessToken(user)
        val newRefreshToken = jwtTokenPort.generateRefreshToken(user)
        
        // Delete old refresh token and save new one
        refreshTokenRepositoryPort.deleteByToken(refreshTokenRequest.refreshToken)
        val newRefreshTokenEntity = RefreshToken(
            token = newRefreshToken,
            userId = user.id,
            expiryDate = LocalDateTime.now().plusSeconds(jwtTokenPort.getRefreshTokenExpiration() / 1000)
        )
        refreshTokenRepositoryPort.save(newRefreshTokenEntity)
        
        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            expiresIn = jwtTokenPort.getAccessTokenExpiration() / 1000,
            username = user.username,
            role = convertStringToRole(user.role)
        )
    }

    private fun convertStringToRole(roleString: String): AuthResponse.Role {
        return when (roleString.uppercase()) {
            "ADMIN" -> AuthResponse.Role.ADMIN
            "USER" -> AuthResponse.Role.USER
            else -> AuthResponse.Role.USER // default to USER if unknown
        }
    }
} 