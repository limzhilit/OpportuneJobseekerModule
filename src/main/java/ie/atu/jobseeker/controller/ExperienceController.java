package ie.atu.jobseeker.controller;

import ie.atu.jobseeker.model.Experience;
import ie.atu.jobseeker.services.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobseeker/experience")
@RequiredArgsConstructor
public class ExperienceController {

  ExperienceService experienceService;

  @GetMapping
  public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
    return ResponseEntity.ok(experienceService.getAll(token));
  }

  @PostMapping
  public ResponseEntity<?> add(@RequestHeader("Authorization") String token, @RequestBody Experience experience) {
    return ResponseEntity.ok(experienceService.add(token, experience));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Experience experience) {
    return ResponseEntity.ok(experienceService.update(token, id, experience));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable Long id) {
    experienceService.delete(token, id);
    return ResponseEntity.noContent().build();
  }
}