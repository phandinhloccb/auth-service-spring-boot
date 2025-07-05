package com.loc.auth_service.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.loc.auth_service.domain.dto.ApiResponse
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        
        val errorResponse = ApiResponse<Any>(
            success = false,
            message = "Unauthorized: ${authException.message}"
        )
        
        val objectMapper = ObjectMapper()
        val responseBody = objectMapper.writeValueAsString(errorResponse)
        
        response.writer.write(responseBody)
    }
} 