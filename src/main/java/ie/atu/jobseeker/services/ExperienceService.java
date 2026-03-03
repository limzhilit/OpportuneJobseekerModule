package ie.atu.jobseeker.services;

import ie.atu.jobseeker.Jobseeker;
import ie.atu.jobseeker.security.JwtUtil;
import ie.atu.jobseeker.model.Experience;
import ie.atu.jobseeker.repository.JobseekerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
public class ExperienceService {

  JwtUtil jwtService;
  JobseekerRepository jobseekerRepository;

  public List<Experience> getAll(@RequestHeader("Authorization") String token) {
    String username = jwtService.extractUsername(token.replace("Bearer ", ""));
    Jobseeker jobseeker = jobseekerRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Profile not found"));
    return ResponseEntity.ok(jobseeker.getExperiences());
  }
}
