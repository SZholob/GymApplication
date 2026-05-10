package com.epam.project.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);
    private final Validator validator;

    public ValidationService() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Універсальний метод для валідації будь-якого об'єкта.
     */
    public <T> void validate(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object to validate cannot be null.");
        }

        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Validation failed for " + object.getClass().getSimpleName() + ": ");
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("; ");
            }
            String errorMessage = sb.toString();
            logger.error(errorMessage);

            throw new IllegalArgumentException(errorMessage);
        }
    }
}