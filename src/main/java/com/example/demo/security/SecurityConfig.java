package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig
{


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((authorizeHttpRequests) ->
						                       authorizeHttpRequests
								                       .requestMatchers("/v1/blobs/**").authenticated() // Add authentication requirement
								                       .anyRequest().permitAll() // Optional: allow other requests if needed

				                      ).sessionManagement(session ->
						                                          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
				.csrf(csrf -> csrf.disable()); // Add this if you're not using CSRF protection
		return http.build();
	}}

