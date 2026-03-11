package ie.atu.jobseeker.services;

import ie.atu.jobseeker.model.Jobseeker;
import ie.atu.jobseeker.repository.JobseekerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobseekerService extends BaseService {

  private final JobseekerRepository jobseekerRepo;

  public Jobseeker upsertJobseeker(String token, Jobseeker incoming) {
    Long userId = getCurrentUserId();
    incoming.setUserId(userId);

    return jobseekerRepo.findByUserId(userId)
        .map(existing -> {
          existing.setName(incoming.getName());
          existing.setGender(incoming.getGender());
          existing.setEthnicity(incoming.getEthnicity());
          existing.setLocation(incoming.getLocation());
          existing.setDob(incoming.getDob());
          existing.getPhone().setCountryCode(incoming.getPhone().getCountryCode());
          existing.getPhone().setNumber(incoming.getPhone().getNumber());
          existing.setLinks(incoming.getLinks());
          return jobseekerRepo.save(existing); // UPDATE
        })
        .orElseGet(() -> jobseekerRepo.save(incoming)); // INSERT
  }

  public Jobseeker getJobseeker(String token) {
    Long userId = getCurrentUserId();
    return jobseekerRepo.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Jobseeker profile not found for user: " + userId));
  }
}
