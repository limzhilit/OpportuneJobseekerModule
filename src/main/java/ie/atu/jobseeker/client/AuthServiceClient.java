package ie.atu.jobseeker.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class AuthServiceClient {

  private final RestClient restClient;

  public AuthServiceClient(@Value("${auth.service.url}") String authServiceUrl) {
    this.restClient = RestClient.builder()
        .baseUrl(authServiceUrl)
        .build();
  }

  public Map<String, String> refresh(String refreshToken) {
    return restClient.post()
        .uri("/auth/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Map.of("refreshToken", refreshToken)) // ✅ matches @CookieValue or @RequestBody on auth service
        .retrieve()
        .body(new ParameterizedTypeReference<Map<String, String>>() {});
  }
}