package ua.khylko.moviedb.dto.user.request;

import lombok.Getter;
import lombok.Setter;
import ua.khylko.moviedb.model.user.Address;

import javax.validation.constraints.*;

@Getter
@Setter
public class UserUpdateRequestDTO {
    @NotBlank(message = "Username can't be empty")
    @Size(min = 6, max = 30,
            message = "Username can be 6-30 characters long")
    @Pattern(regexp = "^[A-Za-z0-9]*$",
            message = "Username can contain only letters and numbers")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, max = 50,
            message = "Password can be 8-50 characters long")
    private String password;

    @NotBlank(message = "First name can't be empty")
    @Size(min = 2, max = 50,
            message = "First name can be 2-50 characters long")
    @Pattern(regexp = "^[A-Z][a-z]*$",
            message = "First name can contain only letters and have to start with capital letter")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    @Size(min = 2, max = 50,
            message = "Last name can be 2-50 characters long")
    @Pattern(regexp = "^[A-Z][a-z]*$",
            message = "Last name can contain only letters and have to start with capital letter")
    private String lastName;

    @NotNull(message = "Age can't be empty")
    @Max(value = 125,
            message = "Age can't exceed 125 years")
    @Min(value = 18,
            message = "18 years is minimal age for registration")
    private Integer age;

    @NotBlank(message = "Email can't be empty")
    @Size(min = 6, max = 65,
            message = "Email can be 6-65 characters long")
    @Email(message = "Incorrect email pattern. Valid email example: john1995@gmail.com")
    private String email;

    @NotBlank(message = "Phone number can't be empty")
    @Size(min = 5, max = 25,
            message = "Phone number can be 5-25 numbers long")
    @Pattern(regexp = "^\\+\\d{3}[- ]?\\d{2}[- ]?\\d{3}[- ]?\\d{2}[- ]?\\d{2}$",
            message = "Incorrect phone number pattern. Valid phone number example: +380 97 200 25 20")
    private String phoneNumber;

    @NotNull(message = "Address can't be empty")
    private Address address;
}
