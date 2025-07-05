package com.loc.auth_service.infrastructure.security

import com.loc.auth_service.application.service.UserService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userService: UserService
) : OncePerRequestFilter() {
    
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        
        try {
            val jwt = authHeader.substring(7)
            val username = jwtService.extractUsername(jwt)
            
            if (username.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
                val userDetails: UserDetails = userService.loadUserByUsername(username)
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        } catch (ex: ExpiredJwtException) {
            logger.error("JWT token is expired: ${ex.message}")
        } catch (ex: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: ${ex.message}")
        } catch (ex: MalformedJwtException) {
            logger.error("JWT token is malformed: ${ex.message}")
        } catch (ex: SignatureException) {
            logger.error("JWT signature is invalid: ${ex.message}")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT token compact of handler are invalid: ${ex.message}")
        }
        
        filterChain.doFilter(request, response)
    }
} 