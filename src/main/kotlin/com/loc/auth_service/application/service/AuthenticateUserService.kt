package com.loc.auth_service.application.service

import com.loc.auth_service.application.port.UserRepositoryPort
import com.loc.auth_service.application.port.PasswordEncoderPort
import com.loc.auth_service.application.port.JwtTokenPort
import com.loc.auth_service.application.port.RefreshTokenRepositoryPort
import com.loc.auth_service.domain.model.User
import com.loc.auth_service.domain.model.RefreshToken
import com.loc.auth_service.domain.dto.AuthRequest
import com.loc.auth_service.domain.dto.AuthResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthenticateUserService(
    private val userRepositoryPort: UserRepositoryPort,
    private val passwordEncoderPort: PasswordEncoderPort,
    private val jwtTokenPort: JwtTokenPort,
    private val refreshTokenRepositoryPort: RefreshTokenRepositoryPort
) {
    
    fun authenticate(authRequest: AuthRequest): AuthResponse {
        val user = userRepositoryPort.findByUsername(authRequest.username)
            ?: throw RuntimeException("User not found")
        
        if (!passwordEncoderPort.matches(authRequest.password, user.password ?: "")) {
            throw RuntimeException("Invalid credentials")
        }
        
        val accessToken = jwtTokenPort.generateAccessToken(user)
        val refreshToken = jwtTokenPort.generateRefreshToken(user)
        
        // Save refresh token
        val refreshTokenEntity = RefreshToken(
            token = refreshToken,
            userId = user.id,
            expiryDate = LocalDateTime.now().plusSeconds(jwtTokenPort.getRefreshTokenExpiration() / 1000)
        )
        refreshTokenRepositoryPort.save(refreshTokenEntity)
        
        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtTokenPort.getAccessTokenExpiration() / 1000,
            username = user.username,
            role = user.role
        )
    }
} 