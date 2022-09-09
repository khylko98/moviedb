package ua.khylko.moviedb.service.user;

import ua.khylko.moviedb.model.user.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    void save(User user);
    void update(String username, User updatedUser);
    void delete(String username);
    void enrichUserToSave(User user);
    void enrichUserToUpdate(User user, User updatedUser);
}
