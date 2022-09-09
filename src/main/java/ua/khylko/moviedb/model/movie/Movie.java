package ua.khylko.moviedb.model.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Title can't be empty")
    @Size(min = 1, max = 100,
            message = "Title can be 1-100 characters long")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Release date can't be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    private Date releaseDate;

    @NotNull(message = "Director can't be empty")
    @ManyToOne
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private Director director;

    @NotNull(message = "Actors can't be empty")
    @ManyToMany
    @JoinTable(
            name = "Movie_Actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;
}
