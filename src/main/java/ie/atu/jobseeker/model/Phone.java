package ie.atu.jobseeker.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Phone {

  @Pattern(regexp = "^\\+?[0-9]{1,4}$", message = "Country code must be valid")
  private String countryCode; // e.g. "+353", "+1"

  @Pattern(regexp = "^([0-9]{7,15})?$", message = "Phone number must be valid")
  private String number;
}
