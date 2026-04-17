package co.edu.cun.ms_backend_java_persons.persons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "co.edu.cun.ms_backend_java_persons")
public class PersonsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonsApplication.class, args);
	}
}
