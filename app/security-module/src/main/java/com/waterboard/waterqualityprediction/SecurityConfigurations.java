package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.filters.AccessDeniedExceptionHandler;
import com.waterboard.waterqualityprediction.filters.ExceptionHandlerFilter;
import com.waterboard.waterqualityprediction.filters.JWTAuthEntryHandler;
import com.waterboard.waterqualityprediction.filters.JWTSecurityFilter;
import com.waterboard.waterqualityprediction.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@Slf4j
public class SecurityConfigurations {

    @Autowired
    RequestDataProvider requestDataProvider;
    @Autowired
    UserService userService;
    @Autowired
    GlobalConfigs globalConfigs;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/user/login", "/user/password-reset/otp", "/user/password-reset/token", "/user/password-reset")
                                .permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new JWTAuthEntryHandler())
                                .accessDeniedHandler(new AccessDeniedExceptionHandler())
                )
                .addFilterBefore(new JWTSecurityFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                                userService, globalConfigs, requestDataProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JWTSecurityFilter.class)
                .addFilterBefore(new LoggingFilter(requestDataProvider), ExceptionHandlerFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setExposedHeaders(List.of("Authorization","Keep-Alive"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}