package architecture.services.interfaces;

import architecture.domain.models.serviceModels.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void registerUser(UserServiceModel userServiceModel);
}
