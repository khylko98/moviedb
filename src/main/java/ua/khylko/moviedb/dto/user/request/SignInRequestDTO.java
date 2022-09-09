package ua.khylko.moviedb.dto.user.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignInRequestDTO {
    @NotBlank(message = "Username can't be empty")
    @Size(min = 5, max = 30,
            message = "Username can be 6-30 characters long")
    @Pattern(regexp = "^[A-Za-z0-9]*$",
            message = "Username can contain only letters and numbers")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, max = 50,
            message = "Password can be 8-50 characters long")
    private String password;
}
