package com.loc.auth_service.domain.model

import java.time.LocalDateTime

data class User(
    val id: Long = 0,
    val username: String,
    val password: String? = null,
    val email: String,
    val role: String = "USER",
    val isEnabled: Boolean = true,
    val isAccountNonExpired: Boolean = true,
    val isAccountNonLocked: Boolean = true,
    val isCredentialsNonExpired: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) 