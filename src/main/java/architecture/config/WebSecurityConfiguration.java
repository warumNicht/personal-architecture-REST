package architecture.config;

import architecture.config.jwt.JWTCsrfTokenRepository;
import architecture.config.jwt.JwtCsrfValidatorFilter;
import architecture.config.jwt.SecretService;
import architecture.constants.AppConstants;
import architecture.error.CustomAccessDeniedHandler;
import architecture.services.interfaces.LocaleService;
import architecture.services.interfaces.UserService;
import architecture.web.filters.CorsFilter;
import architecture.web.filters.CsrfGrantingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.SessionManagementFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userDetailsService;

    @Autowired
    private LocaleService localeService;

    @Autowired
    SecretService secretService;

    @Autowired
    JWTCsrfTokenRepository jwtCsrfTokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint =
                new CustomLoginUrlAuthenticationEntryPoint("/users/login", this.localeService);
        return loginUrlAuthenticationEntryPoint;
    }

    @Bean
    CorsFilter corsFilter() {
        CorsFilter filter = new CorsFilter();
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(new JwtCsrfValidatorFilter(this.secretService), CsrfFilter.class)
                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .addFilterAfter(new CsrfGrantingFilter(), SessionManagementFilter.class)
                .csrf()
                .requireCsrfProtectionMatcher(new NoAntPathRequestMatcher())
                .csrfTokenRepository(this.jwtCsrfTokenRepository)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()//allow CORS option calls
                .antMatchers("/**/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(loginUrlAuthenticationEntryPoint())
                .and()
                .formLogin()
                .loginPage("/users/login")
                .and()
                .logout()
                .logoutSuccessHandler((req, res, auth) -> {   // Logout handler called after successful logout
                    String contextPath = Arrays.stream(req.getCookies())
                            .filter(c -> c.getName().equals(AppConstants.LOCALE_COOKIE_NAME))
                            .findFirst().orElse(null).getValue();
                    req.getSession().setAttribute("message", "You are logged out successfully.");
                    res.sendRedirect("/" + contextPath + "/"); // Redirect user to login page with message.
                })
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
