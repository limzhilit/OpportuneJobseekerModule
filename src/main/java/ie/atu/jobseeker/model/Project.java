package ie.atu.jobseeker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import jakarta.validation.constraints.*;

@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String location;

  @Size(max = 300, message = "Description must be at most 300 characters")
  private String description;

  @Size(max = 5, message = "Please insert the 5 best skill")
  private List<@Size(max = 15, message = "Skill can only have 15 characters") String> skills;

  @PositiveOrZero(message = "Managed head count cannot be negative")
  private Integer managedHeadCount;

  @PositiveOrZero(message = "Project value cannot be negative")
  private Integer projectValue;
}