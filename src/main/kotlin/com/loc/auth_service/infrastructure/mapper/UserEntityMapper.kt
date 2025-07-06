package com.loc.auth_service.infrastructure.mapper

import com.loc.auth_service.domain.model.User
import com.loc.auth_service.infrastructure.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserEntityMapper {
    
    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            password = entity.password,
            email = entity.email,
            role = entity.role,
            isEnabled = entity.enabled,
            isAccountNonExpired = entity.accountNonExpired,
            isAccountNonLocked = entity.accountNonLocked,
            isCredentialsNonExpired = entity.credentialsNonExpired,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            username = domain.username,
            password = domain.password ?: "",
            email = domain.email,
            role = domain.role,
            enabled = domain.isEnabled,
            accountNonExpired = domain.isAccountNonExpired,
            accountNonLocked = domain.isAccountNonLocked,
            credentialsNonExpired = domain.isCredentialsNonExpired,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }
} 