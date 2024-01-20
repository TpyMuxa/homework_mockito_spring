package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.example.exception.IncorrectNameException;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public String validate(String name) {

        if (!StringUtils.isAlpha(name)) {
            throw new IncorrectNameException();
        }
        return StringUtils.capitalize(StringUtils.lowerCase(name));
    }
}
