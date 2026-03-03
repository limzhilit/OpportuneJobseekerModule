package ie.atu.jobseeker.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.*;


@Getter
@Setter
@Table(
    name = "jobseeker",
    uniqueConstraints = @UniqueConstraint(name = "uk_jobseeker_user_id", columnNames = "user_id")
)
@Entity
public class Jobseeker {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false, updatable = false)
  private Long userId;

  private String name;

  private Date dateOfBirth;

  private String gender;

  private String ethnicity;

  private String location;

  @Valid
  @Embedded
  private Phone phone;

  @ElementCollection
  @CollectionTable(name = "jobseeker_links", joinColumns = @JoinColumn(name = "jobseeker_id"))
  @Column(name = "link")
  @Size(max = 5, message = "You can add at most 5 links")
  @Size(max = 5, message = "You can add at most 5 links")
  private List< @Size(max = 200, message = "Link must be at most 200 characters") String> links;

  @OneToMany(mappedBy = "jobseeker", cascade = CascadeType.ALL, orphanRemoval = true)
  @Size(max = 20, message = "You can add at most 20 experiences")
  private List<@Valid Experience> experiences; // ensures nested validation in Experience class
}