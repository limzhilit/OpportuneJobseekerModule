package ie.atu.jobseeker.services;

import ie.atu.jobseeker.security.JwtUtil;
import ie.atu.jobseeker.model.Jobseeker;
import ie.atu.jobseeker.repository.JobseekerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobseekerService {

  private final JobseekerRepository jobseekerRepo;
  private final JwtUtil jwtUtil;

  public Jobseeker upsertJobseeker(String token, Jobseeker incoming) {
    String jwt = token.replace("Bearer ", "");
    Long userId = Long.parseLong(jwtUtil.extractUserId(jwt));
    incoming.setUserId(userId);

    return jobseekerRepo.findByUserId(userId)
        .map(existing -> {
          existing.setName(incoming.getName());
          existing.setGender(incoming.getGender());
          existing.setEthnicity(incoming.getEthnicity());
          existing.setLocation(incoming.getLocation());
          existing.setDateOfBirth(incoming.getDateOfBirth());
          existing.getPhone().setCountryCode(incoming.getPhone().getCountryCode());
          existing.getPhone().setNumber(incoming.getPhone().getNumber());
          return jobseekerRepo.save(existing); // UPDATE
        })
        .orElseGet(() -> jobseekerRepo.save(incoming)); // INSERT
  }
}
