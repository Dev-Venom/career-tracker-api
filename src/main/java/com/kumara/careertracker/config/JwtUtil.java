package com.kumara.careertracker.config;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET = "REMOVED_JWT_SECRET";

	private final long ACCESS_EXPIRATION = 1000 * 60 * 15;
	private final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7;

	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

	public String generateToken(String email) {

		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(key).compact();
	}

	public String extractEmail(String token) {

		Claims claims = Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token)
				.getPayload();

		return claims.getSubject();
	}

	public boolean validateToken(String token) {

		try {

			Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token);

			return true;

		} catch (Exception e) {

			return false;
		}
	}

	public String generateAccessToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min
				.signWith(key).compact();
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 days
				.signWith(key).compact();
	}

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public boolean isTokenValid(String token, String username) {
		return extractUsername(token).equals(username) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}

	private Claims getClaims(String token) {
		return ((JwtParserBuilder) Jwts.builder()).setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

}