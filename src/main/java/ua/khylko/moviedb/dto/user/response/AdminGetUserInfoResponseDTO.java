package ua.khylko.moviedb.dto.user.response;

import lombok.Getter;
import lombok.Setter;
import ua.khylko.moviedb.model.movie.Movie;
import ua.khylko.moviedb.model.user.Address;
import ua.khylko.moviedb.model.user.DateAudit;
import ua.khylko.moviedb.model.user.Role;

import java.util.List;

@Getter
@Setter
public class AdminGetUserInfoResponseDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String phoneNumber;
    private Address address;
    private DateAudit dateAudit;
    private Role role;
    private List<Movie> watchedMovies;
}
