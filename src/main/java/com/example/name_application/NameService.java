package com.example.name_application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Logger;

/**
 * NameServices uses NameRepository to fetch the data from database. Contains most of the service logic and CRUD methods.
 */
@Service
public class NameService {
    private static final Logger logger = Logger.getLogger(NameApplication.class.getName());
    private final NameRepository nameRepository;

    public NameService(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
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

    public void saveNameFromJson(Name name) {
        nameRepository.save(name);
    }

    public void emptyDatabase() {
        nameRepository.deleteAll();
    }
}
