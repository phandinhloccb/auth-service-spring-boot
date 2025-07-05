package com.loc.auth_service.domain.repository

import com.loc.auth_service.domain.entity.RefreshToken
import com.loc.auth_service.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun findByUser(user: User): List<RefreshToken>
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    fun deleteByUser(user: User)
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.user = :user")
    fun revokeAllByUser(user: User)
} 