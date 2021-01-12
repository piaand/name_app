package com.example.name_application;

import org.springframework.stereotype.Service;

@Service
public class NameService {

    private final NameRepository nameRepository;

    public NameService(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    public Long getTotalAmountOfNames() {
        return nameRepository.findTotalAmount();
    }

    public void saveNameFromJson(Name name) {
        nameRepository.save(name);
    }
}
