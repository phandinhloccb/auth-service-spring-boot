package com.loc.auth_service.infrastructure.config

import com.loc.auth_service.application.service.UserService
import com.loc.auth_service.domain.entity.User
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        seedUsers()
    }
    
    private fun seedUsers() {
        // Create admin user if not exists
        if (!userService.existsByUsername("admin")) {
            val adminUser = User(
                username = "admin",
                password = passwordEncoder.encode("admin123"),
                email = "admin@example.com",
                role = "ADMIN"
            )
            userService.save(adminUser)
            println("Admin user created: admin/admin123")
        }
        
        // Create regular user if not exists
        if (!userService.existsByUsername("user")) {
            val regularUser = User(
                username = "user",
                password = passwordEncoder.encode("user123"),
                email = "user@example.com",
                role = "USER"
            )
            userService.save(regularUser)
            println("Regular user created: user/user123")
        }
    }
} 