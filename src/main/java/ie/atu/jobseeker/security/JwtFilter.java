package ie.atu.jobseeker.security;

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
    if (path.startsWith("/h2-console")) {
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

      if (!jwtUtil.isTokenValid(token)) {
        filterChain.doFilter(request, response);
        return;
      }

      String userId = jwtUtil.extractUserId(token);
      System.out.println("Extracted userId: " + userId);

      List<String> roles = jwtUtil.extractRoles(token);

      List<SimpleGrantedAuthority> authorities =
          roles.stream()
              .map(SimpleGrantedAuthority::new)
              .toList();

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              userId,
              null,
              authorities
          );

      authentication.setDetails(
          new WebAuthenticationDetailsSource()
              .buildDetails(request)
      );

      SecurityContextHolder.getContext()
          .setAuthentication(authentication);

    } catch (Exception ex) {
      logger.warn("JWT validation failed", ex);
    }

    filterChain.doFilter(request, response);
  }


}