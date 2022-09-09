package ua.khylko.moviedb.exception.manager;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ExceptionManager {
    public static String returnMessageForException(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
            message .append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage());
        return message.toString();
    }
}
