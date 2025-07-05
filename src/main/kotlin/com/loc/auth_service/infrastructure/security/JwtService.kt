package com.loc.auth_service.infrastructure.security

import com.loc.auth_service.domain.entity.User
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService {
    
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String
    
    @Value("\${jwt.expiration}")
    private var jwtExpiration: Long = 0
    
    @Value("\${jwt.refresh-expiration}")
    private var refreshExpiration: Long = 0
    
    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }
    
    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }
    
    fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }
    
    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return buildToken(extraClaims, userDetails, jwtExpiration)
    }
    
    fun generateRefreshToken(userDetails: UserDetails): String {
        return buildToken(HashMap(), userDetails, refreshExpiration)
    }
    
    fun getJwtExpiration(): Long {
        return jwtExpiration
    }
    
    fun getRefreshExpiration(): Long {
        return refreshExpiration
    }
    
    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long
    ): String {
        val claims = HashMap<String, Any>()
        claims.putAll(extraClaims)
        
        if (userDetails is User) {
            claims["role"] = userDetails.role
            claims["email"] = userDetails.email
            claims["userId"] = userDetails.id
        }
        
        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }
    
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
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