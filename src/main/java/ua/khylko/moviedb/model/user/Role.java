package ua.khylko.moviedb.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.khylko.moviedb.utils.enums.RoleName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Role name can't be empty")
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }
}
