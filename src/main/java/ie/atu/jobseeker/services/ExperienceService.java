package ie.atu.jobseeker.services;

import ie.atu.jobseeker.model.Jobseeker;
import ie.atu.jobseeker.repository.ExperienceRepository;
import ie.atu.jobseeker.security.JwtUtil;
import ie.atu.jobseeker.model.Experience;
import ie.atu.jobseeker.repository.JobseekerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceService {

  private final JwtUtil jwtService;
  private final JobseekerRepository jobseekerRepository;
  private final ExperienceRepository experienceRepository;

  public List<Experience> getAll(@RequestHeader("Authorization") String token) {
    String userId = jwtService.extractUserId(token.replace("Bearer ", ""));
    Jobseeker jobseeker = jobseekerRepository.findByUserId(Long.valueOf(userId))
        .orElseThrow(() -> new RuntimeException("Profile not found"));
    return jobseeker.getExperiences();
  }

  public Experience add(String token, Experience experience) {
    String userId = jwtService.extractUserId(token.replace("Bearer ", ""));
    Jobseeker jobseeker = jobseekerRepository.findByUserId(Long.valueOf(userId))
        .orElseThrow(() -> new RuntimeException("Profile not found"));
    experience.setJobseeker(jobseeker);
    jobseeker.getExperiences().add(experience);
    jobseekerRepository.save(jobseeker);
    return experience;
  }

  public Experience update(String token, Long id, Experience experience) {
    String userId = jwtService.extractUserId(token.replace("Bearer ", ""));
    Jobseeker jobseeker = jobseekerRepository.findByUserId(Long.valueOf(userId))
        .orElseThrow(() -> new RuntimeException("Profile not found"));

    Experience existing = jobseeker.getExperiences().stream()
        .filter(e -> e.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Experience not found"));

    BeanUtils.copyProperties(experience, existing, "id", "jobseeker");
    return experienceRepository.save(existing);
  }

  public void delete(String token, Long id) {
    String userId = jwtService.extractUserId(token.replace("Bearer ", ""));
    Jobseeker jobseeker = jobseekerRepository.findByUserId(Long.valueOf(userId))
        .orElseThrow(() -> new RuntimeException("Profile not found"));

    Experience existing = jobseeker.getExperiences().stream()
        .filter(e -> e.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Experience not found"));

    jobseeker.getExperiences().remove(existing);
    jobseekerRepository.save(jobseeker); // orphanRemoval=true will delete it
  }


}
