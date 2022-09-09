package ua.khylko.moviedb.model.movie;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
@Table(name = "Directors")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "First name can't be empty")
    @Size(min = 2, max = 50,
            message = "First name can be 2-50 characters long")
    @Pattern(regexp = "^[A-Z][a-z]*$",
            message = "First name can contain only letters and have to start with capital letter")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    @Size(min = 2, max = 50,
            message = "Last name can be 2-50 characters long")
    @Pattern(regexp = "^[A-Z][a-z]*$",
            message = "Last name can contain only letters and have to start with capital letter")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Age can't be empty")
    @Max(value = 125,
            message = "Age can't exceed 125 years")
    @Min(value = 18,
            message = "18 years is minimal age for registration")
    @Column(name = "age")
    private Integer age;
}
