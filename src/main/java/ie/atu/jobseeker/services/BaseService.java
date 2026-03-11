package ie.atu.jobseeker.services;

import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService {

  protected Long getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) { // ✅ check this
      throw new RuntimeException("Not authenticated");
    }
    return Long.parseLong((String) authentication.getPrincipal());
  }
}