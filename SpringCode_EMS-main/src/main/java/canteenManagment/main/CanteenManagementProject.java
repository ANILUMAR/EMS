package canteenManagment.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication(scanBasePackages ="canteenManagment.main")
@EntityScan(basePackages = "canteenManagment.main.UserEntity") 
public class CanteenManagementProject{

	public static void main(String[] args) {
		 
		 ConfigurableApplicationContext context = SpringApplication.run(CanteenManagementProject.class, args);
		    System.out.println("Application: Started sucessfully main method ");

	}

}
