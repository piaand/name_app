package com.example.name_application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

    public JsonNode getTotalAmountOfNames() {
        Long result = nameRepository.findTotalAmount() != null ? nameRepository.findTotalAmount() : 0L;
        return turnLongToJson(result);
    }

    //Finds the first name that matches the target name or sends responses code 404 if no name found
    public JsonNode getGivenNameAmount(String targetName) {
        Name result = nameRepository.findByName(targetName);
        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "name not found"
            );
        } else {
            Long amount = result.getAmount();
            return turnLongToJson(amount);
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

    public JsonNode turnLongToJson(Long number) {
        try {
            logger.info("Start parsing number to json array");
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode result = mapper.createObjectNode();
            result.put("totalAmount", number);
            return result;
        } catch (Exception e) {
            logger.severe("Cannot return number as json. Error: " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED, "Result cannot be returned."
            );
        }
    }

    public ObjectNode turnStringListToJson(List<String> names) {
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
                ObjectNode resultNode = mapper.createObjectNode();
                resultNode.set("data", arrayNode);
                return resultNode;
            } catch (Exception e) {
                logger.severe("Cannot return string list as json. Error: " + e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED, "Result cannot be returned."
                );
            }
        }
    }

    public ObjectNode turnNameListToJson(List<NameWrapper> names) {
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
                ObjectNode resultNode = mapper.createObjectNode();
                resultNode.set("data", arrayNode);
                return resultNode;
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

        public ObjectNode listEndPoints() {
            String endPoint1 = "/names - return all the names and their amounts, sort by popularity";
            String endPoint2 = "/names/alphabetical - return all the names sorted alphabetically";
            String endPoint3 = "/names/{name} - return amount of the name that is give as a path variable";
            String endPoint4 = "/names/amount - return the total amount of all names in the record";

            List<String> list = new ArrayList<>();
            list.add(endPoint1);
            list.add(endPoint2);
            list.add(endPoint3);
            list.add(endPoint4);
            return turnStringListToJson(list);
        }
    }