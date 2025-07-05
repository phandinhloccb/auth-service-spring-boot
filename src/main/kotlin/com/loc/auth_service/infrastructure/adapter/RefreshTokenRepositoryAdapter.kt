package com.loc.auth_service.infrastructure.adapter

import com.loc.auth_service.application.port.RefreshTokenRepositoryPort
import com.loc.auth_service.domain.model.RefreshToken
import com.loc.auth_service.infrastructure.repository.RefreshTokenJpaRepository
import com.loc.auth_service.infrastructure.mapper.RefreshTokenEntityMapper
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class RefreshTokenRepositoryAdapter(
    private val refreshTokenJpaRepository: RefreshTokenJpaRepository,
    private val refreshTokenEntityMapper: RefreshTokenEntityMapper
) : RefreshTokenRepositoryPort {
    
    override fun findByToken(token: String): RefreshToken? {
        return refreshTokenJpaRepository.findByToken(token)
            .map { refreshTokenEntityMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun findByUserId(userId: Long): List<RefreshToken> {
        return refreshTokenJpaRepository.findByUserId(userId)
            .map { refreshTokenEntityMapper.toDomain(it) }
    }
    
    override fun save(refreshToken: RefreshToken): RefreshToken {
        val entity = refreshTokenEntityMapper.toEntity(refreshToken)
        val savedEntity = refreshTokenJpaRepository.save(entity)
        return refreshTokenEntityMapper.toDomain(savedEntity)
    }
    
    override fun deleteByToken(token: String) {
        refreshTokenJpaRepository.deleteByToken(token)
    }
    
    override fun deleteByUserId(userId: Long) {
        refreshTokenJpaRepository.deleteByUserId(userId)
    }
    
    override fun revokeAllByUserId(userId: Long) {
        refreshTokenJpaRepository.revokeAllByUserId(userId)
    }
} 