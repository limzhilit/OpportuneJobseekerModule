package ie.atu.jobseeker.repository;

import ie.atu.jobseeker.model.Jobseeker;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobseekerRepository extends JpaRepository<Jobseeker, Long> {

  Optional<Jobseeker> findByUserId(Long userId);

  boolean existsByUserId(Long userId);

  @EntityGraph(attributePaths = "experiences")
  Optional<Jobseeker> findWithExperiencesByUserId(Long userId);
}
