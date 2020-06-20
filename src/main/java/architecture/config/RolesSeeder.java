package architecture.config;

import architecture.domain.entities.auth.Role;
import architecture.domain.entities.auth.UserRoles;
import architecture.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RolesSeeder {
    private final RoleRepository roleRepository;

    @Autowired
    public RolesSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        if (this.roleRepository.count() == 0) {
            Role user = new Role(UserRoles.ROLE_USER);
            Role admin = new Role(UserRoles.ROLE_ADMIN);
            this.roleRepository.save(user);
            this.roleRepository.save(admin);
        }
    }
}
