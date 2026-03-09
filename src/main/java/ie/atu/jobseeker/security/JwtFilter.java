package ie.atu.jobseeker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    String path = request.getRequestURI();
    System.out.println("Path: " + path);
    // Skip H2 console in dev
    String servletPath = request.getServletPath();
    if (servletPath != null && servletPath.startsWith("/h2-console")) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader("Authorization");

    System.out.println("=== JWT FILTER ===");
    System.out.println("Auth header: " + authHeader);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      var auth = SecurityContextHolder.getContext().getAuthentication();
      System.out.println("Auth before filter: " + auth);
      System.out.println("Is authenticated: " + (auth != null && auth.isAuthenticated()));
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.replace("Bearer ", "");


    try {

      Claims claims = jwtUtil.validateToken(token);
      setAuthentication(claims, request);

    } catch (ExpiredJwtException ex) {
      // ✅ Attempt refresh HERE in the filter
      String refreshToken = request.getHeader("Refresh-Token");
      if (refreshToken != null) {
        try {
          String newAccessToken = authServiceClient.refresh(refreshToken);
          response.setHeader("Authorization", "Bearer " + newAccessToken);
          Claims claims = jwtUtil.validateToken(newAccessToken);
          setAuthentication(claims, request);
        } catch (Exception refreshEx) {
          SecurityContextHolder.clearContext(); // refresh failed → entrypoint handles it
        }
      } else {
        SecurityContextHolder.clearContext(); // no refresh token → entrypoint handles it
      }

    } catch (Exception ex) {
      logger.warn("JWT validation failed", ex);
    }

    filterChain.doFilter(request, response);
  }

  private void setAuthentication(Claims claims, HttpServletRequest request) {
    String userId = claims.getSubject();

    // ✅ "role" is a String, not a List
    String role = claims.get("role", String.class);
    List<SimpleGrantedAuthority> authorities = role != null
        ? List.of(new SimpleGrantedAuthority("ROLE_" + role))
        : List.of();

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userId, null, authorities);

    authentication.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}