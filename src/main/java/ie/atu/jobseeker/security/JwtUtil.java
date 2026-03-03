package ie.atu.jobseeker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  // Use SecretKey for better type safety in 0.12.x
  private javax.crypto.SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /*
  What it does (Step-by-Step)
    Starts the Parser: Jwts.parser() initializes the engine used to read JWT strings.
    Verifies Signature: .verifyWith(getSigningKey()) uses your private secret key to recalculate the digital signature. If even one character in the token was changed by a hacker, this step will throw an exception.
    Parses Claims: .parseSignedClaims(token) breaks the string into its original components (Header, Payload, Signature).
    Extracts Payload: .getPayload() returns the Claims object, which contains the user's data (email, roles, expiration date).
   */
  public Claims validateToken(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String extractUserId(String token) {
    try {
      return validateToken(token).getSubject();
    } catch (JwtException | IllegalArgumentException e) {
      return null;
    }
  }

  public boolean isTokenValid(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public List<String> extractRoles(String token) {
    try {
      return validateToken(token).get("roles", List.class);
    } catch (JwtException | IllegalArgumentException e) {
      return null;
    }
  }
}
