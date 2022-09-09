package ua.khylko.moviedb.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.khylko.moviedb.model.user.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
