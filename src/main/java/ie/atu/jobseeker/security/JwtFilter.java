package ie.atu.jobseeker.security;

import ie.atu.jobseeker.client.AuthServiceClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final AuthServiceClient authServiceClient;

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
      System.out.println("Token expired - attempting refresh");
      Cookie[] cookies = request.getCookies();
      String refreshToken = null;
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if ("refreshToken".equals(cookie.getName())) {
            refreshToken = cookie.getValue();
            break;
          }
        }
      }

      if (refreshToken == null) {
        System.out.println("No refresh token found");
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Token expired, no refresh token");
        return; // ✅ stop here
      } else {
        try {
          Map<String, String> tokens = authServiceClient.refresh(refreshToken);
          String newAccessToken = tokens.get("accessToken");
          response.setHeader("Authorization", "Bearer " + newAccessToken);
          Claims claims = jwtUtil.validateToken(newAccessToken);
          setAuthentication(claims, request);
        } catch (Exception refreshEx) {
          logger.warn("Refresh failed", refreshEx);
          SecurityContextHolder.clearContext();
        }
      }
    } catch (Exception ex) {
      System.out.println("Token failed - returning 401");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid token");
      return;
    }
    filterChain.doFilter(request, response);
  }

  private void setAuthentication(Claims claims, HttpServletRequest request) {
    String userId = claims.getSubject();

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