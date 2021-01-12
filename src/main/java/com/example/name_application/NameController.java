package com.example.name_application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NameController {

    private final NameService nameService;

    public NameController (NameService nameService) {
        this.nameService = nameService;
    }

    @GetMapping("/names/amount")
    public Long getTotalAmountOfNames() {
        return nameService.getTotalAmountOfNames();
    }


}
