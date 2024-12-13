package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtTokenFilter extends OncePerRequestFilter
{


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.info("Received Request");
		try {
			String token = extractTokenFromRequest(request);
			System.out.println("token = " + token);
			if (token != null && JwtProvider.validateToken(token)) {
				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken("test", null, Collections.emptyList()));
				logger.error("Token is valid");

			} else {
				// If no valid token, send unauthorized response
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				logger.error("Token is invalid");

				return;
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			// Log the exception
			logger.error("Authentication error", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
