package com.example.name_application;

import org.springframework.stereotype.Service;

/**
 * NameServices uses NameRepository to fetch the data from database. Contains most of the service logic and CRUD methods.
 */
@Service
public class NameService {

    private final NameRepository nameRepository;

    public NameService(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    public Long getTotalAmountOfNames() {
        Long result = nameRepository.findTotalAmount() != null ? nameRepository.findTotalAmount() : 0L;
        return result;
    }

    public void saveNameFromJson(Name name) {
        nameRepository.save(name);
    }

    public void emptyDatabase() {
        nameRepository.deleteAll();
    }
}
