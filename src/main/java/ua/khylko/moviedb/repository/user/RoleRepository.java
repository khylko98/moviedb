package ua.khylko.moviedb.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.khylko.moviedb.model.user.Role;
import ua.khylko.moviedb.utils.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleName roleName);
}
