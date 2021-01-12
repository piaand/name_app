package com.example.name_application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
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
				JsonNode nameObj = mapper.readTree(inputStream);
				JsonNode listNames = nameObj.get("names");
				System.out.println(listNames);
				List<Name> names = mapper.convertValue(listNames, typeReference);
				for(Name name : names) {
					nameService.saveNameFromJson(name);
					System.out.println(name + " saved to db!");
				}
			} catch (Exception e){
				System.out.println("Unable to save users: " + e.getMessage());
			}
		};
	}

}
