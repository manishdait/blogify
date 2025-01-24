package com.example.blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.blog.user.role.Role;
import com.example.blog.user.role.RoleRepository;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class BlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
			Role role = Role.builder().role("Role_USER").build();
			roleRepository.save(role);
		};
	}
}
