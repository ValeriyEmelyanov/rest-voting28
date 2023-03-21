package com.example.restvoting28;

import com.example.restvoting28.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class RestVoting28Application {
	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(RestVoting28Application.class, args);
	}

}
