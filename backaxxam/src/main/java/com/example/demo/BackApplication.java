package com.example.demo;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.entities.Category;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.security.entities.AppRole;
import com.example.demo.security.entities.AppUser;
import com.example.demo.security.services.AccountService;

@SpringBootApplication
public class BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	            // Save roles
//	            accountService.addNewRole(adminRole);
//	            accountService.addNewRole(userRole);
//	            accountService.addNewRole(hostRole);
	            // Create initial user
//	            AppUser admin = AppUser.builder()
//	                    .username("admin")
//	                    .password("admin123") // Encode the password
//	                    .email("admin@example.com")
//	                    .fullname("Administrator")
//	                    .appRoles(List.of(adminRole))
//	                    .build();

	@Bean
	CommandLineRunner commandLineRunner(AccountService accountService, PasswordEncoder passwordEncoder,
			CategoryRepository categoryRepository) {
		return args -> {
			// Create initial roles
			AppRole adminRole = new AppRole(null, "ROLE_ADMIN");
			AppRole hostRole = new AppRole(null, "ROLE_HOST");
			AppRole userRole = new AppRole(null, "ROLE_USER");

			// Save roles
//			accountService.addNewRole(adminRole);
//			accountService.addNewRole(userRole);
//			accountService.addNewRole(hostRole);
			// Create initial user
			AppUser admin = AppUser.builder().username("admin").password("admin123") // Encode the password
					.email("admin@example.com").fullname("Administrator").appRoles(List.of(adminRole)).build();

			AppUser user = AppUser.builder().username("user").password("user123") // Encode the password
					.email("user@example.com").fullname("Normal User").appRoles(List.of(userRole)).build();

			AppUser host = AppUser.builder().username("host").password("host123")
					.email("host@example.com").fullname("Host User").appRoles(List.of(hostRole)).build();

			// Save users
//	            accountService.addNewUser(admin);
//	            accountService.addNewUser(user);
//	            accountService.addNewUser(host);

			Category category1 = Category.builder().name("Technology").build();
			Category category2 = Category.builder().name("Health").build();
			Category category3 = Category.builder().name("Travel").build();

//				categoryRepository.save(category1);
//	            categoryRepository.save(category2);
//	            categoryRepository.save(category3);
		};
	}

}