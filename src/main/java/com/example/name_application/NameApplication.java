package com.example.name_application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
public class NameApplication {

	private static final Logger logger = Logger.getLogger(NameApplication.class.getName());
	private static final String filePath = "/json/names.json";
	private final LogConfigurations configurations;

	public NameApplication(LogConfigurations configurations) {
		this.configurations = configurations;
	}

	public static void main(String[] args) {
		SpringApplication.run(NameApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(NameService nameService) {
		return args -> {
			configurations.setConfig();
			logger.info("Start reading names from json file: " + filePath);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Name>> typeReference = new TypeReference<List<Name>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream(filePath);

			try {
				JsonNode nameObj = mapper.readTree(inputStream);
				JsonNode listNames = nameObj.get("names");
				List<Name> names = mapper.convertValue(listNames, typeReference);
				for(Name name : names) {
					nameService.saveNameFromJson(name);
				}
				logger.info("All names read to the database");
			} catch (Exception e){
				logger.severe("Unable to save users: " + e.getMessage());
			}
		};
	}

}
