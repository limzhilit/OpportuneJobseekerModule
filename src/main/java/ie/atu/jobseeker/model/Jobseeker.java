package ie.atu.jobseeker.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.*;


@Getter
@Setter
@Table(
    name = "jobseeker",
    uniqueConstraints = @UniqueConstraint(name = "uk_jobseeker_user_id", columnNames = "user_id")
)
@Entity
@RequiredArgsConstructor
public class Jobseeker {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false, updatable = false)
  private Long userId;

  private String name;

  private LocalDate dob;

  private String gender;

  private String ethnicity;

  private String location;

  @Valid
  @Embedded
  private Phone phone;



  @ElementCollection
  @CollectionTable(
      name = "jobseeker_links",
      joinColumns = @JoinColumn(name = "jobseeker_id")
  )
  @MapKeyColumn(name = "label")
  @Column(name = "url")
  @Size(max = 5, message = "You can add at most 5 links")
  private Map<
        @Size(max = 50, message = "Link name must be at most 50 characters") String,
        @Size(max = 200, message = "URL must be at most 200 characters") String
        > links;

}