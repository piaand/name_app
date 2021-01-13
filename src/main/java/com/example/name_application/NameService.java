package com.example.name_application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Logger;

/**
 * NameServices uses NameRepository to fetch the data from database. Contains most of the service logic and CRUD methods.
 */
@Service
public class NameService {
    private static final Logger logger = Logger.getLogger(NameApplication.class.getName());
    private final NameRepository nameRepository;
    private final NameWrapper wrapper;

    public NameService(NameRepository nameRepository, NameWrapper wrapper) {
        this.nameRepository = nameRepository;
        this.wrapper = wrapper;
    }

    public Long getTotalAmountOfNames() {
        Long result = nameRepository.findTotalAmount() != null ? nameRepository.findTotalAmount() : 0L;
        return result;
    }

    //Finds the first name that matches the target name or sends responses code 404 if no name found
    public Long getGivenNameAmount(String targetName) {
        Name result = nameRepository.findByName(targetName);
        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "name not found"
            );
        } else {
            Long amount = result.getAmount();
            return amount;
        }
    }

    public JsonNode getAllNamesSortedAlphabetically(){
        try {
            logger.info("Request to find all names alphabetically in progress.");
            List<Name> listNames = nameRepository.findAllByOrderByNameAsc();
            List<String> wrappedNames= wrapper.convertNamesToWrappedNames(listNames);
            return turnStringListToJson(wrappedNames);
        } catch (ConversionFailedException e) {
            logger.severe("Cannot return namelist from db. Error: " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED, "Result cannot be returned."
            );
        }
    }

    public void saveNameFromJson(Name name) {
        nameRepository.save(name);
    }

    public void emptyDatabase() {
        nameRepository.deleteAll();
    }

    public ArrayNode turnStringListToJson(List<String> names) {
        if (names.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No name is found"
            );
        } else {
            try {
                logger.info("Start parsing string list to json array");
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode arrayNode = mapper.createArrayNode();
                for (String name : names) {
                    JsonNode node = mapper.convertValue(name, JsonNode.class);
                    arrayNode.add(node);
                }
                return arrayNode;
            } catch (Exception e) {
                logger.severe("Cannot return string list as json. Error: " + e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED, "Result cannot be returned."
                );
            }
        }
    }

    public ArrayNode turnNameListToJson(List<NameWrapper> names) {
        if (names.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No name is found"
            );
        } else {
            try {
                logger.info("Start parsing list to json array");
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode arrayNode = mapper.createArrayNode();
                for (NameWrapper name : names) {
                    JsonNode node = mapper.convertValue(name, JsonNode.class);
                    arrayNode.add(node);
                }
                return arrayNode;
            } catch (Exception e) {
                logger.severe("Cannot return namelist as json. Error: " + e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED, "Result cannot be returned."
                );
            }
        }
    }

        public JsonNode getAllNamesAndAmounts() {
            try {
                logger.info("Request to find all names and amounts in progress.");
                List<Name> listNames = nameRepository.findAllByOrderByAmountDesc();
                List<NameWrapper> wrappedNames= wrapper.convertNamesToWrappedNamesAndAmount(listNames);
                return turnNameListToJson(wrappedNames);
            } catch (ConversionFailedException e) {
                logger.severe("Cannot return namelist from db. Error: " + e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED, "Result cannot be returned."
                );
            }
        }
    }