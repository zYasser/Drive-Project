package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtProvider
{

	//NOTE: you should expose JWT Secret but for simplicity and easier testing i will expose it here.
	private static String jwtSecret = "do_not_use_in_production_842383018312389123091283";

	public static String generateToken(String username) {
		return Jwts.builder().setSubject(username).signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes()).setIssuedAt(
				new Date()).setIssuer("Driver").setExpiration(new Date(System.currentTimeMillis() + 3600000)).compact();
	}

	public static boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			System.out.println("e = " + e);
			return false;
		}
	}

}


