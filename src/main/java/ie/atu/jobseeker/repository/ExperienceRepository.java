package ie.atu.jobseeker.repository;

import ie.atu.jobseeker.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
