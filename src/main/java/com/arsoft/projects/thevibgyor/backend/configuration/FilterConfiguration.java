package com.arsoft.projects.thevibgyor.backend.configuration;

import com.arsoft.projects.thevibgyor.backend.constant.ENDPOINT;
import com.arsoft.projects.thevibgyor.backend.filter.JwtAuthenticationFilter;
import com.arsoft.projects.thevibgyor.backend.filter.TokenRequestFilter;
import com.arsoft.projects.thevibgyor.backend.security.auth.token.service.JwtService;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.arsoft.projects.thevibgyor.backend.constant.ENDPOINT.ALL_ENDPOINTS;

@Configuration
public class FilterConfiguration {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public FilterRegistrationBean<TokenRequestFilter> tokenRequestFilter(UserService userService) {
        FilterRegistrationBean<TokenRequestFilter> registrationBean = new FilterRegistrationBean<>();
        TokenRequestFilter tokenRequestFilter = new TokenRequestFilter(userService);
        registrationBean.setFilter(tokenRequestFilter);
        registrationBean.addUrlPatterns(ENDPOINT.GET_TOKEN_ENDPOINT.getValue());
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> JwtAuthenticationFilter(UserService userService) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        JwtAuthenticationFilter tokenRequestFilter = new JwtAuthenticationFilter(jwtService, userService, objectMapper);
        registrationBean.setFilter(tokenRequestFilter);
        registrationBean.addUrlPatterns(ENDPOINT.ADMIN_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue(), ENDPOINT.LOGGED_USER_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue());
        return registrationBean;
    }
}
