package com.ulutman.security.jwt;

import com.ulutman.repository.UserRepository;
import com.ulutman.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthService authService) throws Exception {
        http.cors(cors -> {
                    cors.configurationSource(request -> {
                        var corsConfiguration = new CorsConfiguration();
                        corsConfiguration.addAllowedOrigin("*");
                        corsConfiguration.addAllowedMethod("*");
                        corsConfiguration.addAllowedHeader("*");
                        return corsConfiguration;
                    });
                }).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/api/manage/**",
                                        "/api/manage/users",
                                        "/api/manage/complaints",
                                        "/api/manage/moderator",
                                        "/api/manage/publishes",
                                        "/api/manage/mailing",
                                        "api/payment/**",
                                        "/api/mailing",
                                        "/api/manage/category").hasAuthority("ADMIN")
                                .requestMatchers(
                                        "/",
                                        "/login", "/oauth2/**",
                                        "/auth/google-login",
                                        "/api/auth/**",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/api/publishes/**",
                                        "/api/users/**",
                                        "/api/users/messages",
                                        "/api/users/comments",
                                        "/api/users/complaints",
                                        "/api/mailing/sendPasswordResetCode",
                                        "/api/mailing/resetPassword",
                                        "/api/S3/upload",
                                        "/api/favorites/check",
                                        "/api/user-accounts/{userId}",
                                        "/api/main-page/**",
                                        "/advertising/**")
                                .permitAll()
                                .requestMatchers("/api/users/my-publishes/**").hasAuthority("USER")
                                .anyRequest()
                                .authenticated())
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь с email: " + email + " не существует"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
