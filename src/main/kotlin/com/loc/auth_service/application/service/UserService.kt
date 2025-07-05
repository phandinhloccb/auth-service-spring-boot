package com.loc.auth_service.application.service

import com.loc.auth_service.domain.entity.User
import com.loc.auth_service.domain.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) : UserDetailsService {
    
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found with username: $username") }
    }
    
    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username).orElse(null)
    }
    
    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }
    
    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
    
    fun save(user: User): User {
        return userRepository.save(user)
    }
} 