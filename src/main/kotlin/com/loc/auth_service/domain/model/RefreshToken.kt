package com.loc.auth_service.domain.model

import java.time.LocalDateTime

data class RefreshToken(
    val id: Long = 0,
    val token: String,
    val userId: Long,
    val expiryDate: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isRevoked: Boolean = false
) {
    fun isExpired(): Boolean {
        return LocalDateTime.now().isAfter(expiryDate)
    }
} 