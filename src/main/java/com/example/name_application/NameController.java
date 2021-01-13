package com.example.name_application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API controller. Collection of different end-points user may call.
 * /names/amount - return total amount of all names in the database
 */
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
