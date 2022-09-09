package ua.khylko.moviedb.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "Addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Street can't be empty")
    @Size(min = 5, max = 100,
            message = "Street can be 5-100 characters long")
    @Pattern(regexp = "^[A-Z]([a-z0-9]+[- .]?)+$",
            message = "Street can contain only letters and have to start with capital letter")
    @Column(name = "street")
    private String street;

    @NotBlank(message = "City can't be empty")
    @Size(min = 5, max = 50,
            message = "City can be 5-50 characters long")
    @Pattern(regexp = "^[A-Z]([a-z0-9]+[- .]?)+$",
            message = "City can contain only letters and have to start with capital letter")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "Country can't be empty")
    @Size(min = 5, max = 50,
            message = "Country can be 5-50 characters long")
    @Pattern(regexp = "^[A-Z]([a-z0-9]+[- .]?)+$",
            message = "Country can contain only letters and have to start with capital letter")
    @Column(name = "country")
    private String country;

    @NotBlank(message = "Zip code can't be empty")
    @Pattern(regexp = "\\d{5}",
            message = "Incorrect zip code pattern. Valid zip code example: 49654")
    @Column(name = "zip_code")
    private String zipCode;
}
