package architecture.domain.entities.auth;

import architecture.constants.AppConstants;
import architecture.domain.entities.BaseEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {
    @Column(name = "authority", unique = true, nullable = false, length = AppConstants.USER_ROLE_MAX_LENGTH)
    @Enumerated(value = EnumType.STRING)
    private UserRoles authority;

    public Role() {
    }

    public Role(UserRoles authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority.toString();
    }

    public void setAuthority(UserRoles authority) {
        this.authority = authority;
    }

}
