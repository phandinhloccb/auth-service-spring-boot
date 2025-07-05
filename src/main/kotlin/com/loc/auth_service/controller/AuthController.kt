package com.loc.auth_service.controller

import com.loc.auth_service.application.service.AuthenticateUserService
import com.loc.auth_service.application.service.RefreshTokenService
import com.loc.auth_service.application.service.LogoutUserService
import com.loc.auth_service.application.service.ValidateTokenService
import com.loc.auth_service.controller.mapper.AuthRequestMapper
import com.loc.auth_service.controller.mapper.AuthResponseMapper
import com.loc.auth_service.domain.dto.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticateUserService: AuthenticateUserService,
    private val refreshTokenService: RefreshTokenService,
    private val logoutUserService: LogoutUserService,
    private val validateTokenService: ValidateTokenService,
    private val authRequestMapper: AuthRequestMapper,
    private val authResponseMapper: AuthResponseMapper
) {
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody authRequest: AuthRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        return try {
            val authResponse = authenticateUserService.authenticate(authRequest)
            ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    message = "Login successful",
                    data = authResponse
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
                    message = "Login failed: ${e.message}"
                )
            )
        }
    }
    
    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        return try {
            val authResponse = refreshTokenService.refreshToken(refreshTokenRequest)
            ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    message = "Token refreshed successfully",
                    data = authResponse
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
                    message = "Token refresh failed: ${e.message}"
                )
            )
        }
    }
    
    @PostMapping("/logout")
    fun logout(@Valid @RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<ApiResponse<String>> {
        return try {
            logoutUserService.logout(refreshTokenRequest)
            ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    message = "Logout successful"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
                    message = "Logout failed: ${e.message}"
                )
            )
        }
    }
    
    @PostMapping("/logout-all")
    fun logoutAll(authentication: Authentication): ResponseEntity<ApiResponse<String>> {
        return try {
            logoutUserService.logoutAll(authentication.name)
            ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    message = "Logout from all devices successful"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
                    message = "Logout from all devices failed: ${e.message}"
                )
            )
        }
    }
    
    @GetMapping("/validate")
    fun validateToken(@RequestHeader("Authorization") authHeader: String): ResponseEntity<ApiResponse<Map<String, Any>>> {
        return try {
            val token = authHeader.substring(7) // Remove "Bearer " prefix
            val user = validateTokenService.validateToken(token)
            
            val userData = mapOf(
                "username" to user.username,
                "role" to user.role,
                "email" to user.email,
                "userId" to user.id
            )
            
            ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    message = "Token is valid",
                    data = userData
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
                    message = "Token validation failed: ${e.message}"
                )
            )
        }
    }
} 