package com.loc.auth_service.infrastructure.repository

import com.loc.auth_service.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): Optional<UserEntity>
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
} 