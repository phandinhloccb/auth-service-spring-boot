package com.loc.auth_service.infrastructure.adapter

import com.loc.auth_service.application.port.UserRepositoryPort
import com.loc.auth_service.domain.model.User
import com.loc.auth_service.infrastructure.mapper.UserEntityMapper
import com.loc.auth_service.infrastructure.repository.UserJpaRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val userEntityMapper: UserEntityMapper
) : UserRepositoryPort {
    
    override fun findByUsername(username: String): User? {
        return userJpaRepository.findByUsername(username)
            .map { userEntityMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun findById(id: Long): User? {
        return userJpaRepository.findById(id)
            .map { userEntityMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun save(user: User): User {
        val entity = userEntityMapper.toEntity(user)
        val savedEntity = userJpaRepository.save(entity)
        return userEntityMapper.toDomain(savedEntity)
    }
    
    override fun existsByUsername(username: String): Boolean {
        return userJpaRepository.existsByUsername(username)
    }
    
    override fun existsByEmail(email: String): Boolean {
        return userJpaRepository.existsByEmail(email)
    }
} 