package com.loc.auth_service.application.port

import com.loc.auth_service.domain.model.RefreshToken

interface RefreshTokenRepositoryPort {
    fun findByToken(token: String): RefreshToken?
    fun findByUserId(userId: Long): List<RefreshToken>
    fun save(refreshToken: RefreshToken): RefreshToken
    fun deleteByToken(token: String)
    fun deleteByUserId(userId: Long)
    fun revokeAllByUserId(userId: Long)
} 