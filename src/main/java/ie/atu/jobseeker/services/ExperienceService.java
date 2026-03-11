package ie.atu.jobseeker.services;

import ie.atu.jobseeker.repository.ExperienceRepository;
import ie.atu.jobseeker.security.JwtUtil;
import ie.atu.jobseeker.model.Experience;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceService extends BaseService{

  private final JwtUtil jwtUtil;
  private final ExperienceRepository experienceRepository;

  @Transactional
  public Experience add(String token, Experience experience) {
    Long userId = getCurrentUserId();
    System.out.println("Extracted userId: " + userId);

    experience.setUserId(userId);
    experience.setId(null); // Ensure it's a new record
    return experienceRepository.save(experience);
  }

  @Transactional
  public Experience update(String token, Long id, Experience experience) {
    Long userId = getCurrentUserId();

    // Verify the experience exists and belongs to this user
    Experience existing = experienceRepository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new RuntimeException("Experience not found or access denied"));

    // Copy properties but preserve id and userId
    BeanUtils.copyProperties(experience, existing, "id", "userId");

    return experienceRepository.save(existing);
  }

  @Transactional
  public void delete(String token, Long id) {
    Long userId = getCurrentUserId();

    // Verify ownership before deleting
    Experience existing = experienceRepository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new RuntimeException("Experience not found or access denied"));

    experienceRepository.delete(existing);
  }

  @Transactional
  public List<Experience> saveAll(String token, List<Experience> experiences) {
    Long userId = getCurrentUserId();

    // Delete all existing experiences for this user
    experienceRepository.deleteByUserId(userId);

    // Set userId for all new experiences
    experiences.forEach(exp -> {
      exp.setId(null); // Ensure new records
      exp.setUserId(userId);
    });

    // Save all new experiences
    return experienceRepository.saveAll(experiences);
  }

  @Transactional
  public List<Experience> getAll(String token) {
    Long userId = getCurrentUserId();
    return experienceRepository.findByUserId(userId);
  }
}
