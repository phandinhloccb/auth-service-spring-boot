package com.loc.auth_service.infrastructure.adapter

import com.loc.auth_service.application.port.JwtTokenPort
import com.loc.auth_service.domain.model.User
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.function.Function

@Component
class JwtTokenAdapter : JwtTokenPort {
    
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String
    
    @Value("\${jwt.expiration}")
    private var jwtExpiration: Long = 0
    
    @Value("\${jwt.refresh-expiration}")
    private var refreshExpiration: Long = 0
    
    override fun generateAccessToken(user: User): String {
        return buildToken(user, jwtExpiration)
    }
    
    override fun generateRefreshToken(user: User): String {
        return buildToken(user, refreshExpiration)
    }
    
    override fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }
    
    override fun isTokenValid(token: String, user: User): Boolean {
        val username = extractUsername(token)
        return (username == user.username) && !isTokenExpired(token)
    }
    
    override fun getAccessTokenExpiration(): Long {
        return jwtExpiration
    }
    
    override fun getRefreshTokenExpiration(): Long {
        return refreshExpiration
    }
    
    private fun buildToken(user: User, expiration: Long): String {
        val claims = HashMap<String, Any>()
        claims["role"] = user.role
        claims["email"] = user.email
        claims["userId"] = user.id
        
        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(user.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }
    
    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }
    
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }
    
    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }
    
    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }
    
    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
} 