package com.loc.auth_service.application.port

import com.loc.auth_service.domain.model.User

interface UserRepositoryPort {
    fun findByUsername(username: String): User?
    fun findById(id: Long): User?
    fun save(user: User): User
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
} 