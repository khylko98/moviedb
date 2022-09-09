package ua.khylko.moviedb.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.khylko.moviedb.model.user.DateAudit;

@Repository
public interface DateAuditRepository extends JpaRepository<DateAudit, Long> {
}
