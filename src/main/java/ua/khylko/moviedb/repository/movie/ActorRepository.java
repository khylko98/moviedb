package ua.khylko.moviedb.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.khylko.moviedb.model.movie.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    boolean existsActorByFirstNameAndLastName(String fistName, String lastName);
    Actor findByFirstNameAndLastName(String fistName, String lastName);
}
