package com.arsoft.projects.thevibgyor.backend.configuration;

import com.arsoft.projects.thevibgyor.backend.constant.ENDPOINT;
import com.arsoft.projects.thevibgyor.backend.filter.JwtAuthenticationFilter;
import com.arsoft.projects.thevibgyor.backend.filter.RequestTimeFilter;
import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.security.auth.CustomTokenRequestAuthenticationHandler;
import com.arsoft.projects.thevibgyor.backend.security.auth.CustomTokenRequestAuthorizationHandler;
import com.arsoft.projects.thevibgyor.backend.security.auth.token.service.JwtService;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.arsoft.projects.thevibgyor.backend.constant.ENDPOINT.ALL_ENDPOINTS;


@Slf4j
@Configuration
public class SecurityConfiguration {
    @Autowired
    private CustomTokenRequestAuthenticationHandler customTokenRequestAuthenticationHandler;
    @Autowired
    private CustomTokenRequestAuthorizationHandler customTokenRequestAuthorizationHandler;
    @Autowired
    private CustomTokenRequestAuthenticationHandler customRequestAuthenticationHandler;
    @Autowired
    private CustomTokenRequestAuthorizationHandler customRequestAuthorizationHandler;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
        http
                //This ensures that this filterChain gets applies to GET_TOKEN_ENDPOINT only
                .securityMatcher(ENDPOINT.GET_TOKEN_ENDPOINT.getValue())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(ENDPOINT.LOGGED_USER_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).permitAll()
                        .requestMatchers(ENDPOINT.ADMIN_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).permitAll()
                        .requestMatchers(ENDPOINT.GET_TOKEN_ENDPOINT.getValue()).hasAnyRole(Role.ADMIN.getValue(), Role.USER.getValue(), Role.GUEST.getValue())
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(customTokenRequestAuthenticationHandler)
                        .accessDeniedHandler(customTokenRequestAuthorizationHandler)
                )
                .httpBasic(httpBasicConfigurer ->
                        httpBasicConfigurer.authenticationEntryPoint(customTokenRequestAuthenticationHandler)
                )
                //Add RequestTimeFilter before BasicAuthenticationFilter to set requestTime in each request
                .addFilterBefore(new RequestTimeFilter(), BasicAuthenticationFilter.class);

        log.info("Basic authentication Security configuration has been set up.");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain jwtAuthFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userService, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(ENDPOINT.ADMIN_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).hasAnyRole(Role.ADMIN.getValue())
                        .requestMatchers(ENDPOINT.LOGGED_USER_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).hasAnyRole(Role.ADMIN.getValue(), Role.USER.getValue())
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(customRequestAuthenticationHandler)
                        .accessDeniedHandler(customRequestAuthorizationHandler)
                )
                .httpBasic(httpBasicConfigurer ->
                        httpBasicConfigurer.authenticationEntryPoint(customRequestAuthenticationHandler)
                )
                //Add RequestTimeFilter before JwtAuthenticationFilter to set requestTime in each request
                .addFilterBefore(new RequestTimeFilter(), JwtAuthenticationFilter.class);
        log.info("JWT authentication Security configuration has been set up.");
        return http.build();
    }
}
