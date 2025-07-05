package com.loc.auth_service.application.port

import com.loc.auth_service.domain.model.User

interface JwtTokenPort {
    fun generateAccessToken(user: User): String
    fun generateRefreshToken(user: User): String
    fun extractUsername(token: String): String
    fun isTokenValid(token: String, user: User): Boolean
    fun getAccessTokenExpiration(): Long
    fun getRefreshTokenExpiration(): Long
} 