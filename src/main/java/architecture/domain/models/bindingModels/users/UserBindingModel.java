package architecture.domain.models.bindingModels.users;

import architecture.constants.AppConstants;

import javax.validation.constraints.*;

public abstract class UserBindingModel {
    @NotNull
    @NotEmpty(message = "{text.empty}")
    @Size(min = 3, max = AppConstants.USERNAME_MAX_LENGTH, message = "{text.length.between}")
    @NotBlank(message = "{text.blank}")
    private String username;

    @NotNull
    @NotEmpty(message = "{text.empty}")
    @NotBlank(message = "{text.blank}")
    @Size(min = 4, max = AppConstants.USER_PASSWORD_MAX_LENGTH, message = "{text.length.between}")
    @Pattern(regexp = "^(?=.*[a-z]).+$", message = "{user.password.lowercase}")
    @Pattern(regexp = "^(?=.*[A-Z]).+$", message = "{user.password.uppercase}")
    @Pattern(regexp = "^(?=.*\\d).+$", message = "{user.password.digit}")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
