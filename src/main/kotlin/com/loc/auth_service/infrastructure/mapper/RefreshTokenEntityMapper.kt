package com.loc.auth_service.infrastructure.mapper

import com.loc.auth_service.domain.model.RefreshToken
import com.loc.auth_service.infrastructure.entity.RefreshTokenEntity
import org.springframework.stereotype.Component

@Component
class RefreshTokenEntityMapper {
    
    fun toDomain(entity: RefreshTokenEntity): RefreshToken {
        return RefreshToken(
            id = entity.id,
            token = entity.token,
            userId = entity.userId,
            expiryDate = entity.expiryDate,
            createdAt = entity.createdAt,
            isRevoked = entity.isRevoked
        )
    }
    
    fun toEntity(domain: RefreshToken): RefreshTokenEntity {
        return RefreshTokenEntity(
            id = domain.id,
            token = domain.token,
            userId = domain.userId,
            expiryDate = domain.expiryDate,
            createdAt = domain.createdAt,
            isRevoked = domain.isRevoked
        )
    }
} 