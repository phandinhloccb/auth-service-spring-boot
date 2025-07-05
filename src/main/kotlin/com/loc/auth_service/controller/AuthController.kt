package com.loc.auth_service.controller

import com.loc.auth_service.application.service.AuthenticateUserService
import com.loc.auth_service.application.service.RefreshTokenService
import com.loc.auth_service.application.service.LogoutUserService
import com.loc.authservice.model.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticateUserService: AuthenticateUserService,
    private val refreshTokenService: RefreshTokenService,
    private val logoutUserService: LogoutUserService,
) {
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody authRequest: AuthRequest): ResponseEntity<AuthSuccessResponse> {
        return try {
            val authResponse = authenticateUserService.authenticate(authRequest)
            ResponseEntity.ok(
                AuthSuccessResponse(
                    success = true,
                    message = "Login successful",
                    data = authResponse,
                    timestamp = Instant.now().toEpochMilli()
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                AuthSuccessResponse(
                    success = false,
                    message = "Login failed: ${e.message}",
                    data = null,
                    timestamp = Instant.now().toEpochMilli()
                )
            )
        }
    }
    
    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<AuthSuccessResponse> {
        return try {
            val authResponse = refreshTokenService.refreshToken(refreshTokenRequest)
            ResponseEntity.ok(
                AuthSuccessResponse(
                    success = true,
                    message = "Token refreshed successfully",
                    data = authResponse,
                    timestamp = Instant.now().toEpochMilli()
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                AuthSuccessResponse(
                    success = false,
                    message = "Token refresh failed: ${e.message}",
                    data = null,
                    timestamp = Instant.now().toEpochMilli()
                )
            )
        }
    }
    
    @PostMapping("/logout")
    fun logout(@Valid @RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<SuccessResponse> {
        return try {
            logoutUserService.logout(refreshTokenRequest)
            ResponseEntity.ok(
                SuccessResponse(
                    success = true,
                    message = "Logout successful",
                    timestamp = Instant.now().toEpochMilli()
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                SuccessResponse(
                    success = false,
                    message = "Logout failed: ${e.message}",
                    timestamp = Instant.now().toEpochMilli()
                )
            )
        }
    }
    
    @PostMapping("/logout-all")
    fun logoutAll(authentication: Authentication): ResponseEntity<SuccessResponse> {
        return try {
            logoutUserService.logoutAll(authentication.name)
            ResponseEntity.ok(
                SuccessResponse(
                    success = true,
                    message = "Logout from all devices successful",
                    timestamp = Instant.now().toEpochMilli()
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                SuccessResponse(
                    success = false,
                    message = "Logout from all devices failed: ${e.message}",
                    timestamp = Instant.now().toEpochMilli()
                )
            )
        }
    }
}