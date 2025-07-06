package com.loc.auth_service.infrastructure.repository

import com.loc.auth_service.infrastructure.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByToken(token: String): Optional<RefreshTokenEntity>
    fun findByUserId(userId: Long): List<RefreshTokenEntity>
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.userId = :userId")
    fun deleteByUserId(userId: Long)
    
    @Modifying
    @Query("UPDATE RefreshTokenEntity rt SET rt.isRevoked = true WHERE rt.userId = :userId")
    fun revokeAllByUserId(userId: Long)
    
    fun deleteByToken(token: String)
} 