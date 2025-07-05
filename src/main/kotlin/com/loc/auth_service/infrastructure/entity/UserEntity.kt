package com.loc.auth_service.infrastructure.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(unique = true, nullable = false)
    private val username: String,
    
    @Column(nullable = false)
    private val password: String,
    
    @Column(nullable = false)
    val email: String,
    
    @Column(nullable = false)
    val role: String = "USER",
    
    @Column(nullable = false)
    val enabled: Boolean = true,
    
    @Column(nullable = false)
    val accountNonExpired: Boolean = true,
    
    @Column(nullable = false)
    val accountNonLocked: Boolean = true,
    
    @Column(nullable = false)
    val credentialsNonExpired: Boolean = true,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    val updatedAt: LocalDateTime = LocalDateTime.now()
) : UserDetails {
    
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_$role"))
    }
    
    override fun getPassword(): String = password
    
    override fun getUsername(): String = username
    
    override fun isAccountNonExpired(): Boolean = accountNonExpired
    
    override fun isAccountNonLocked(): Boolean = accountNonLocked
    
    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired
    
    override fun isEnabled(): Boolean = enabled
} 