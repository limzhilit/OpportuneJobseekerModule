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

  public Jobseeker upsertJobseeker(String token, Jobseeker jobseeker) {
    String jwt = token.replace("Bearer ", "");
    String userId = jwtUtil.extractUserId(jwt);
    jobseeker.setUserId(Long.parseLong(userId));
    return jobseekerRepo.save(jobseeker);
  }
}
