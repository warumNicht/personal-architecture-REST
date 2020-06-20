package architecture.repositories;

import architecture.domain.entities.auth.Role;
import architecture.domain.entities.auth.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(UserRoles authority);
}
