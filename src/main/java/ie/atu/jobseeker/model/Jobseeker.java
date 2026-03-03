package ie.atu.jobseeker.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
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
  private long id;

  @Column(name = "user_id", nullable = false, updatable = false)
  private long userId;

  private String name;

  @Min(value = 5, message = "Age must be at least 5")
  @Max(value = 100, message = "Age must not exceed 100")
  private int age;

  @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid")
  private String phone;

  @ElementCollection
  @CollectionTable(name = "jobseeker_links", joinColumns = @JoinColumn(name = "jobseeker_id"))
  @Column(name = "link")
  @Size(max = 5, message = "You can add at most 5 links")
  @Size(max = 5, message = "You can add at most 5 links")
  private List<@NotBlank(message = "Link cannot be blank") @Size(max = 200, message = "Link must be at most 200 characters") String> links;

  @OneToMany(mappedBy = "jobseeker", cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "jobseeker_id")
  @Size(max = 20, message = "You can add at most 20 experiences")
  private List<@Valid Experience> experiences; // ensures nested validation in Experience class
}