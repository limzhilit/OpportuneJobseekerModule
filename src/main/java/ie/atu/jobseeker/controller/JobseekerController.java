package ie.atu.jobseeker.controller;

import ie.atu.jobseeker.model.Jobseeker;
import ie.atu.jobseeker.services.JobseekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/jobseeker")
@RequiredArgsConstructor
public class JobseekerController {

  private final JobseekerService jobseekerService;

  @PutMapping("/api/jobseeker/createProfile")
  public ResponseEntity<?> createProfile(@RequestHeader("Authorization") String token, @RequestBody Jobseeker jobseeker) {
    return ResponseEntity.ok(jobseekerService.upsertJobseeker(token, jobseeker));
  }
}