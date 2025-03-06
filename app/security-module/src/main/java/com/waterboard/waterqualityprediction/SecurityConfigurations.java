package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.filters.AccessDeniedExceptionHandler;
import com.waterboard.waterqualityprediction.filters.ExceptionHandlerFilter;
import com.waterboard.waterqualityprediction.filters.JWTAuthEntryHandler;
import com.waterboard.waterqualityprediction.filters.JWTSecurityFilter;
import com.waterboard.waterqualityprediction.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfigurations {

    private final RequestContextManager requestContextManager;
    private final UserService userService;
    private final GlobalAppConfig globalAppConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/user/login", "/user/password-reset/otp", "/user/password-reset/token", "/user/password-reset")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new JWTAuthEntryHandler())
                                .accessDeniedHandler(new AccessDeniedExceptionHandler())
                )
                .addFilterBefore(new JWTSecurityFilter(authenticationManager(authenticationConfiguration),
                                this.userService, this.globalAppConfig, this.requestContextManager),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JWTSecurityFilter.class)
                .addFilterBefore(new LoggingFilter(requestContextManager), ExceptionHandlerFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");  // Updated method for wildcards
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setExposedHeaders(List.of("Authorization", "Keep-Alive"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
