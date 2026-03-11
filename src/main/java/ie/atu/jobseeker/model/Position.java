package ie.atu.jobseeker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "position")
public class Position {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private Integer yearStart;

  private Integer yearEnd;

  private String description;
  @OneToMany(cascade = CascadeType.ALL)
  private List<Project> projects;
}
