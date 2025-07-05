package com.loc.auth_service.application.service

import com.loc.auth_service.domain.dto.AuthRequest
import com.loc.auth_service.domain.dto.AuthResponse
import com.loc.auth_service.domain.dto.RefreshTokenRequest
import com.loc.auth_service.domain.entity.User
import com.loc.auth_service.infrastructure.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val refreshTokenService: RefreshTokenService
) {
    
    fun authenticate(authRequest: AuthRequest): AuthResponse {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.username,
                authRequest.password
            )
        )
        
        val user = authentication.principal as User
        val jwtToken = jwtService.generateToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)
        
        // Save refresh token to database
        val refreshTokenExpiryDate = LocalDateTime.now().plusSeconds(jwtService.getRefreshExpiration() / 1000)
        refreshTokenService.createRefreshToken(user, refreshToken, refreshTokenExpiryDate)
        
        return AuthResponse(
            accessToken = jwtToken,
            refreshToken = refreshToken,
            expiresIn = jwtService.getJwtExpiration() / 1000,
            username = user.username,
            role = user.role
        )
    }
    
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): AuthResponse {
        val refreshToken = refreshTokenService.findByToken(refreshTokenRequest.refreshToken)
            ?: throw RuntimeException("Refresh token is not in database!")
        
        refreshTokenService.verifyExpiration(refreshToken)
        
        val user = refreshToken.user
        val jwtToken = jwtService.generateToken(user)
        val newRefreshToken = jwtService.generateRefreshToken(user)
        
        // Delete old refresh token and create new one
        refreshTokenService.deleteByToken(refreshTokenRequest.refreshToken)
        val refreshTokenExpiryDate = LocalDateTime.now().plusSeconds(jwtService.getRefreshExpiration() / 1000)
        refreshTokenService.createRefreshToken(user, newRefreshToken, refreshTokenExpiryDate)
        
        return AuthResponse(
            accessToken = jwtToken,
            refreshToken = newRefreshToken,
            expiresIn = jwtService.getJwtExpiration() / 1000,
            username = user.username,
            role = user.role
        )
    }
    
    fun logout(refreshToken: String) {
        refreshTokenService.deleteByToken(refreshToken)
        SecurityContextHolder.clearContext()
    }
    
    fun logoutAll(username: String) {
        val user = userService.findByUsername(username)
            ?: throw RuntimeException("User not found")
        
        refreshTokenService.revokeAllByUser(user)
        SecurityContextHolder.clearContext()
    }
} 