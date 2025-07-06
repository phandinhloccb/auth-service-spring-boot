package com.loc.auth_service.infrastructure.adapter

import com.loc.auth_service.application.port.PasswordEncoderPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoderAdapter(
    private val passwordEncoder: PasswordEncoder
) : PasswordEncoderPort {
    
    override fun encode(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }
    
    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
} 