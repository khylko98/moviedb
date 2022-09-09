package ua.khylko.moviedb.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.khylko.moviedb.model.user.DateAudit;
import ua.khylko.moviedb.model.user.Role;
import ua.khylko.moviedb.model.user.User;
import ua.khylko.moviedb.repository.user.AddressRepository;
import ua.khylko.moviedb.repository.user.DateAuditRepository;
import ua.khylko.moviedb.repository.user.RoleRepository;
import ua.khylko.moviedb.repository.user.UserRepository;
import ua.khylko.moviedb.security.UserDetailsImpl;
import ua.khylko.moviedb.service.user.UserService;
import ua.khylko.moviedb.utils.enums.RoleName;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final DateAuditRepository dateAuditRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AddressRepository addressRepository,
                           DateAuditRepository dateAuditRepository,
                           RoleRepository roleRepository,
                           @Lazy
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.dateAuditRepository = dateAuditRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("No user found for '" + username + "'");
        return new UserDetailsImpl(user.get());
    }

    @Transactional
    @Override
    public void save(User user) {
        enrichUserToSave(user);

        addressRepository.save(user.getAddress());
        dateAuditRepository.save(user.getDateAudit());
        roleRepository.save(user.getRole());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void update(String username, User updatedUser) {
        Optional<User> user = findByUsername(username);

        enrichUserToUpdate(user.get(), updatedUser);

        addressRepository.save(updatedUser.getAddress());
        dateAuditRepository.save(updatedUser.getDateAudit());
        roleRepository.save(updatedUser.getRole());
        userRepository.save(updatedUser);
    }

    @Transactional
    @Override
    public void delete(String username) {
        Optional<User> user = findByUsername(username);

        addressRepository.delete(user.get().getAddress());
        dateAuditRepository.delete(user.get().getDateAudit());
        userRepository.delete(user.get());
    }

    @Override
    public void enrichUserToSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setDateAudit(new DateAudit(new Date(), new Date()));
        user.setRole(roleRepository.findByRoleName(RoleName.ROLE_USER));
    }

    @Override
    public void enrichUserToUpdate(User user, User updatedUser) {
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        user.getDateAudit().setUpdatedAt(new Date());

        updatedUser.getAddress().setId(user.getAddress().getId());
        updatedUser.setDateAudit(user.getDateAudit());
        updatedUser.setRole(user.getRole());
        updatedUser.setId(user.getId());
    }
}
