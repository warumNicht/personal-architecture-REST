package architecture.domain.models.bindingModels.users;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserJwtToken {
    private String username;
    private List<String> roles;

    public UserJwtToken(String username, Collection<GrantedAuthority> roles) {
        this.username = username;
        this.roles = roles.stream().map(r -> r.getAuthority()).collect(Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
