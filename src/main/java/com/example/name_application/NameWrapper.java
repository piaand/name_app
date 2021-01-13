package com.example.name_application;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Data wrapper for name entity that picks the right content for json response
 */
@Component
@Data @AllArgsConstructor @NoArgsConstructor
public class NameWrapper {
    private String name;
    private Long amount;

    public List<NameWrapper> convertNamesToWrappedNames(List<Name> list){
        List<NameWrapper> convertList = new ArrayList<>();
        for (Name name: list) {
            NameWrapper wrappedName = new NameWrapper(name.getName(), name.getAmount());
            convertList.add(wrappedName);
        }
        return convertList;
    }
}
