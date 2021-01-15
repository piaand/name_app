package com.example.name_application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.boot.ApplicationArguments;

/**
 * Name application - application builds an API interface which provides access to a database of names.
 * @author Pia Andersin
 *
 * At the start of the application, names are read from a json file and saved to database.
 */
@SpringBootApplication
public class NameApplication {

	private static final Logger logger = Logger.getLogger(NameApplication.class.getName());
	private static final String filePathDefault = "/json/names.json";
	private static final String pathToResource = "src/main/resources";
	private final LogConfigurations configurations;

	public NameApplication(LogConfigurations configurations) {
		this.configurations = configurations;
	}

	@Autowired
	private org.springframework.boot.ApplicationArguments applicationArguments;

	@Value("${file.path}")
	private String filePathGiven;

	public static void main(String[] args) {
		SpringApplication.run(NameApplication.class, args);
	}


	//CommandLineRunner reads the names from a file and saves them into the database.
	@Bean
	CommandLineRunner runner(NameService nameService) {
		return args -> {
			//Setting the logging configurations for the whole application.
			System.out.println(filePathGiven);
			configurations.setConfig();

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Name>> typeReference = new TypeReference<List<Name>>(){};

			File inputFile = new File(pathToResource + filePathGiven);
			String filePath = filePathGiven;
			if(!inputFile.exists()) {
				logger.warning("Given filepath is not found from " + filePathGiven + " fall back to default filepath");
				filePath = filePathDefault;
				File defaultInputFile = new File(pathToResource + filePath);
				if(!defaultInputFile.exists()) {
					logger.severe("No input to read. Program exits.");
					System.exit(1);
				}
			}

			try {
				InputStream inputStream = TypeReference.class.getResourceAsStream(filePath);
				logger.info("Start reading names from json file: " + filePath);
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
