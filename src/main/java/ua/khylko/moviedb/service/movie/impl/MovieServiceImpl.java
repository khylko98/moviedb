package ua.khylko.moviedb.service.movie.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.khylko.moviedb.model.movie.Actor;
import ua.khylko.moviedb.model.movie.Movie;
import ua.khylko.moviedb.model.user.User;
import ua.khylko.moviedb.repository.movie.ActorRepository;
import ua.khylko.moviedb.repository.movie.DirectorRepository;
import ua.khylko.moviedb.repository.movie.MovieRepository;
import ua.khylko.moviedb.repository.user.UserRepository;
import ua.khylko.moviedb.service.movie.MovieService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;

    @Autowired
    public MovieServiceImpl(UserRepository userRepository,
                            MovieRepository movieRepository,
                            DirectorRepository directorRepository,
                            ActorRepository actorRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
    }

    @Override
    public Optional<Movie> findByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    @Override
    public List<Movie> getMovies(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.get().getWatchedMovies();
    }

    @Transactional
    @Override
    public void addMovie(String username, String title) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Movie> movieToAdd = findByTitle(title);

        user.get().getWatchedMovies().add(movieToAdd.get());
    }

    @Transactional
    @Override
    public void save(Movie movie) {
        saveDirector(movie);
        saveActors(movie);

        movieRepository.save(movie);
    }

    @Override
    public void saveDirector(Movie movie) {
        if (directorRepository.existsDirectorByFirstNameAndLastName(
                movie.getDirector().getFirstName(), movie.getDirector().getLastName()))
            movie.setDirector(directorRepository.findByFirstNameAndLastName(
                    movie.getDirector().getFirstName(), movie.getDirector().getLastName()));
        else
            directorRepository.save(movie.getDirector());
    }

    @Override
    public void saveActors(Movie movie) {
        for (Actor actor : movie.getActors()) {
            if (actorRepository.existsActorByFirstNameAndLastName(
                    actor.getFirstName(), actor.getLastName()))
                movie
                        .getActors()
                        .stream()
                        .filter(
                                actorFromStream ->
                                        actorFromStream.getFirstName().equals(actor.getFirstName())
                                                &&
                                                actorFromStream.getLastName().equals(actor.getLastName()))
                        .findAny()
                        .get()
                        .setId(actorRepository.findByFirstNameAndLastName(
                                        actor.getFirstName(), actor.getLastName())
                                .getId());
        }
        actorRepository.saveAll(movie.getActors());
    }
}
