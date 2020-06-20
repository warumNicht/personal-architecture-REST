package architecture.web.controllers;

import architecture.config.jwt.JWTCsrfTokenRepository;
import architecture.constants.AppConstants;
import architecture.constants.ViewNames;
import architecture.domain.models.bindingModels.users.UserCreateBindingModel;
import architecture.domain.models.bindingModels.users.UserJwtToken;
import architecture.domain.models.bindingModels.users.UserLoginBindingModel;
import architecture.domain.models.serviceModels.UserServiceModel;
import architecture.services.interfaces.UserService;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(value = "/users")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTCsrfTokenRepository jwtCsrfTokenRepository;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, AuthenticationManager authenticationManager, JWTCsrfTokenRepository jwtCsrfTokenRepository) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtCsrfTokenRepository = jwtCsrfTokenRepository;
    }


    @GetMapping(value = "/register")
    public String registerUser(@ModelAttribute(name = ViewNames.USER_REGISTER_binding_model) UserCreateBindingModel model,
                               Principal principal) {
        if (principal == null) {
            return ViewNames.USER_REGISTER;
        }
        return "redirect:/" + super.getLocale() + "/";
    }

    @PostMapping(value = "/register")
    public String registerUserPost(@Valid @ModelAttribute(name = ViewNames.USER_REGISTER_binding_model) UserCreateBindingModel bindingModel,
                                   BindingResult bindingResult, Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/" + super.getLocale() + "/";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute(ViewNames.USER_REGISTER_binding_model, bindingModel);
            return ViewNames.USER_REGISTER;
        }
        if (!bindingModel.getPassword().equals(bindingModel.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "user.password.notMatch");
            model.addAttribute(ViewNames.USER_REGISTER_binding_model, bindingModel);
            return ViewNames.USER_REGISTER;
        }
        UserServiceModel user = this.modelMapper.map(bindingModel, UserServiceModel.class);
        this.userService.registerUser(user);
        return "redirect:/" + super.getLocale() + "/";
    }

    @GetMapping(value = "/login")
    public String loginUser(@ModelAttribute(name = "userLogin") UserLoginBindingModel model,
                            Principal principal) {
        if (principal == null) {
            return ViewNames.USER_LOGIN;
        }
        return "redirect:/" + super.getLocale() + "/";
    }

    @PostMapping(value = "/rest-authentication")
    public ResponseEntity loginRest(@RequestBody UserLoginBindingModel userBinding, ServletRequest request, ServletResponse response){
        System.out.println("loc");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        try {
            UserDetails loggingUser = userService.loadUserByUsername(userBinding.getUsername());
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(loggingUser,
                            userBinding.getPassword(), loggingUser.getAuthorities());

            Authentication authenticate = this.authenticationManager.authenticate(token);

            if (token.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(token);
                super.logger.info(String.format("Login of user: %s, successfully!", userBinding.getUsername()));
                            CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                UserJwtToken userJwtToken = new UserJwtToken(token.getName(), token.getAuthorities());
                CsrfToken csrfToken = this.jwtCsrfTokenRepository.generateLoginToken(userJwtToken);
                this.jwtCsrfTokenRepository.saveToken(csrfToken,httpServletRequest, httpServletResponse);

                String token2 = csrfToken.getToken();
            return  ResponseEntity.ok().body(token2);
            }
        } catch (AuthenticationException e) {
            JSONObject errorObj = new JSONObject();
            errorObj.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorObj.toMap());
        }
        return null;
    }

    @PostMapping(value = "/authentication")
    public String loginUserPost(@ModelAttribute(name = "userLogin") UserLoginBindingModel userBinding,
                                Model model, Principal principal, HttpSession session) {
        if (principal != null) {
            return "redirect:/" + super.getLocale() + "/";
        }

        try {
            UserDetails loggingUser = userService.loadUserByUsername(userBinding.getUsername());
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(loggingUser,
                            userBinding.getPassword(), loggingUser.getAuthorities());

            Authentication authenticate = this.authenticationManager.authenticate(token);

            if (token.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(token);
                super.logger.info(String.format("Login of user: %s, successfully!", userBinding.getUsername()));
            }
        } catch (AuthenticationException e) {
            model.addAttribute("exception", e);
            return ViewNames.USER_LOGIN;
        }
        Object attribute = session.getAttribute(AppConstants.LOGIN_REFERRER_SESSION_ATTRIBUTE_NAME);
        if (attribute != null) {
            session.removeAttribute(AppConstants.LOGIN_REFERRER_SESSION_ATTRIBUTE_NAME);
            return "redirect:/" + super.getLocale() + attribute;
        }
        return "redirect:/" + super.getLocale() + "/";
    }

    @PostMapping (value="/custom-logout")
    public ResponseEntity customLogout(HttpServletRequest request, HttpServletResponse response) {
        // Get the Spring Authentication object of the current request.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // In case you are not filtering the users of this request url.
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication); // <= This is the call you are looking for.
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully logged out");
    }

    @GetMapping (value="/custom-logout2")
    public ResponseEntity customLogout2(HttpServletRequest request, HttpServletResponse response) {
        // Get the Spring Authentication object of the current request.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // In case you are not filtering the users of this request url.
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication); // <= This is the call you are looking for.
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Your age is ");
        }
        return ResponseEntity.ok(HttpStatus.FORBIDDEN);
    }

    @PostConstruct
    public void doLog() {
        super.logger.info("User controller started");
    }
}
