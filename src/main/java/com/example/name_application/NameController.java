package com.example.name_application;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API controller. Collection of different end-points user may call.
 * /names/amount - return total amount of all names in the database
 * /names/{name} - return amount of the name that is give as a path variable
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

    @GetMapping("/names/{name}")
    public Long getName(@PathVariable String name) {
        return nameService.getGivenNameAmount(name);
    }

    @GetMapping("/names")
    public JsonNode getNames() {
        return nameService.getAllNamesAndAmounts();
    }

}
