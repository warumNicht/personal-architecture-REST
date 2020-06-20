package architecture.domain.models.bindingModels.users;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserCreateBindingModel extends UserBindingModel {
    @NotNull
    @NotEmpty(message = "{text.empty}")
    @Email(message = "{user.email}")
    private String email;

    @NotNull
    @NotEmpty(message = "{text.empty}")
    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
