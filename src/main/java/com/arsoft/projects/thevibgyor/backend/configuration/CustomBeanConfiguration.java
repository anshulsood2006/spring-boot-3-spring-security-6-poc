package com.arsoft.projects.thevibgyor.backend.configuration;

import com.arsoft.projects.thevibgyor.backend.exception.ThrowableMixing;
import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.repository.UserRepository;
import com.arsoft.projects.thevibgyor.backend.security.auth.CustomTokenRequestAuthenticationHandler;
import com.arsoft.projects.thevibgyor.backend.security.auth.CustomTokenRequestAuthorizationHandler;
import com.arsoft.projects.thevibgyor.backend.service.UserRepositoryImpl;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Slf4j
@Configuration
public class CustomBeanConfiguration {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("akhil")
                .password(passwordEncoder().encode("sood"))
                .roles(Role.USER.name())
                .build();
        UserDetails admin = User.builder()
                .username("anshul")
                .password(passwordEncoder().encode("sood"))
                .roles(Role.ADMIN.name())
                .build();
        log.info("userDetailsService has been set up.");
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("BCryptPasswordEncoder has been set up.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule()).addMixIn(Throwable.class, ThrowableMixing.class);
    }

    @Bean
    public CustomTokenRequestAuthenticationHandler customTokenRequestAuthenticationHandler(ObjectMapper objectMapper) {
        return new CustomTokenRequestAuthenticationHandler(objectMapper);
    }

    @Bean
    public CustomTokenRequestAuthorizationHandler customTokenRequestAuthorizationHandler(ObjectMapper objectMapper) {
        return new CustomTokenRequestAuthorizationHandler(objectMapper);
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService(userRepository()));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }


    /**
     * This bean removes ROLE_ prefix from all the user roles
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Empty string removes the prefix
    }
}
