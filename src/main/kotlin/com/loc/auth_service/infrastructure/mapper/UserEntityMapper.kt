package com.loc.auth_service.infrastructure.mapper

import com.loc.auth_service.domain.model.User
import com.loc.auth_service.infrastructure.repository.entity.UserEntity
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
            isEnabled = entity.isEnabled,
            isAccountNonExpired = entity.isAccountNonExpired,
            isAccountNonLocked = entity.isAccountNonLocked,
            isCredentialsNonExpired = entity.isCredentialsNonExpired,
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
            isEnabled = domain.isEnabled,
            isAccountNonExpired = domain.isAccountNonExpired,
            isAccountNonLocked = domain.isAccountNonLocked,
            isCredentialsNonExpired = domain.isCredentialsNonExpired,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }
} 