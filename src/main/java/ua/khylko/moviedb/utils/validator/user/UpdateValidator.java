package ua.khylko.moviedb.utils.validator.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.khylko.moviedb.model.user.User;
import ua.khylko.moviedb.service.user.UserService;
import ua.khylko.moviedb.service.user.impl.UserServiceImpl;

@Component
public class UpdateValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UpdateValidator(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.findByUsername(user.getUsername()).isPresent() &&
                !userService.findByUsername(user.getUsername()).get().getId().equals(user.getId()))
            errors.rejectValue( "username",
                            "",
                        "User with username already exist");
        else if (userService.findByEmail(user.getEmail()).isPresent() &&
                !userService.findByUsername(user.getUsername()).get().getId().equals(user.getId()))
            errors.rejectValue( "email",
                            "",
                        "User with email already exist");
    }
}
