package com.example.name_application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class NameApplication {

	public static void main(String[] args) {
		SpringApplication.run(NameApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(NameService nameService) {
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Name>> typeReference = new TypeReference<List<Name>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json/names.json");
			try {
				List<Name> names = mapper.readValue(inputStream,typeReference);
				for(Name name : names) {
					nameService.saveNameFromJson(name);
				}
			} catch (IOException e){
				System.out.println("Unable to save users: " + e.getMessage());
			}
		};
	}

}
