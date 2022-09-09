package ua.khylko.moviedb.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.khylko.moviedb.dto.user.request.SignInRequestDTO;
import ua.khylko.moviedb.dto.user.request.SignUpRequestDTO;
import ua.khylko.moviedb.exception.SignUpException;
import ua.khylko.moviedb.model.user.User;
import ua.khylko.moviedb.security.JwtTokenProvider;
import ua.khylko.moviedb.service.user.UserService;
import ua.khylko.moviedb.service.user.impl.UserServiceImpl;
import ua.khylko.moviedb.utils.validator.user.SignUpValidator;

import javax.validation.Valid;
import java.util.Map;

import static ua.khylko.moviedb.exception.manager.ExceptionManager.returnMessageForException;

@RestController
@RequestMapping("/moviedb/auth")
@Slf4j
public class AuthController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final SignUpValidator signUpValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserServiceImpl userService,
                          ModelMapper modelMapper,
                          SignUpValidator signUpValidator,
                          JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.signUpValidator = signUpValidator;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public Map<String, String> signUp(
            @RequestBody @Valid SignUpRequestDTO signUp, BindingResult bindingResult) {
        User user = converterToUser(signUp);
        signUpValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            throw new SignUpException(returnMessageForException(bindingResult));
        userService.save(user);
        String token = jwtTokenProvider.generateToken(user.getUsername());
        log.info("Sign Up new user with username: {}", user.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/signin")
    public Map<String, String> signIn(
            @RequestBody @Valid SignInRequestDTO signIn) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(signIn.getUsername(), signIn.getPassword());
        authenticationManager.authenticate(authToken);
        String token = jwtTokenProvider.generateToken(signIn.getUsername());
        log.info("Sign In user with username: {}", signIn.getUsername());
        return Map.of("jwt-token", token);
    }

    private User converterToUser(SignUpRequestDTO signUp) {
        return modelMapper.map(signUp, User.class);
    }
}
