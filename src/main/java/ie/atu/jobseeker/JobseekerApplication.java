package ie.atu.jobseeker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
public class JobseekerApplication {

  public static void main(String[] args) {
    SpringApplication.run(JobseekerApplication.class, args);
  }

}
