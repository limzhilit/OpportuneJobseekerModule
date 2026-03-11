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

  @PutMapping("/upsertJobseeker")
  public ResponseEntity<?> upsertJobseeker(@RequestHeader("Authorization") String token, @RequestBody Jobseeker jobseeker) {
    System.out.println("Received jobseeker data: " + jobseeker);
    return ResponseEntity.ok(jobseekerService.upsertJobseeker(token, jobseeker));
  }

  @GetMapping("/getJobseeker")
  public ResponseEntity<?> getJobseeker(@RequestHeader("Authorization") String token) {
    return ResponseEntity.ok(jobseekerService.getJobseeker(token));
  }


}