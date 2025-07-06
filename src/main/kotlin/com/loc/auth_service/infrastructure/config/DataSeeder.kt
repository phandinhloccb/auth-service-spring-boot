package com.loc.auth_service.infrastructure.config

import com.loc.auth_service.application.port.UserRepositoryPort
import com.loc.auth_service.application.port.PasswordEncoderPort
import com.loc.auth_service.domain.model.User
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val userRepositoryPort: UserRepositoryPort,
    private val passwordEncoderPort: PasswordEncoderPort
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        seedUsers()
    }
    
    private fun seedUsers() {
        // Create admin user if not exists
        if (userRepositoryPort.findByUsername("admin") == null) {
            val adminUser = User(
                username = "admin",
                password = passwordEncoderPort.encode("admin123"),
                email = "admin@example.com",
                role = "ADMIN"
            )
            userRepositoryPort.save(adminUser)
            println("Admin user created: admin/admin123")
        }
        
        // Create regular user if not exists
        if (userRepositoryPort.findByUsername("user") == null) {
            val regularUser = User(
                username = "user",
                password = passwordEncoderPort.encode("user123"),
                email = "user@example.com",
                role = "USER"
            )
            userRepositoryPort.save(regularUser)
            println("Regular user created: user/user123")
        }
    }
} 