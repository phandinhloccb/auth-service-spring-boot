package com.loc.auth_service.presentation.controller

import com.loc.auth_service.application.service.AuthService
import com.loc.auth_service.domain.dto.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody authRequest: AuthRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        return try {
            val authResponse = authService.authenticate(authRequest)
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
            val authResponse = authService.refreshToken(refreshTokenRequest)
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
            authService.logout(refreshTokenRequest.refreshToken)
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
            authService.logoutAll(authentication.name)
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
    fun validateToken(authentication: Authentication): ResponseEntity<ApiResponse<Map<String, Any>>> {
        return try {
            val userDetails = authentication.principal as com.loc.auth_service.domain.entity.User
            val userData = mapOf(
                "username" to userDetails.username,
                "role" to userDetails.role,
                "email" to userDetails.email,
                "userId" to userDetails.id
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