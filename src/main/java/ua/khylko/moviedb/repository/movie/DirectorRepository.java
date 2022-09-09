package ua.khylko.moviedb.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.khylko.moviedb.model.movie.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    boolean existsDirectorByFirstNameAndLastName(String firstName, String lastName);
    Director findByFirstNameAndLastName(String firstName, String lastName);
}
