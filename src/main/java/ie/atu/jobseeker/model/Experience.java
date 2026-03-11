package ie.atu.jobseeker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "experience")
public class Experience {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String organisation;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Position> positions;

  @Column(name = "user_id", nullable = false)
  private Long userId;  
}
