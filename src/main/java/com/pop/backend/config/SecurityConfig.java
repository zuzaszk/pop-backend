package com.pop.backend.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import com.pop.backend.auth.CustomOAuth2UserService;
import com.pop.backend.auth.JwtFilter;
import com.pop.backend.auth.OAuth2SuccessHandler;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler OAuth2SuccessHandler;
    private final JwtFilter jwtFilter;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, OAuth2SuccessHandler OAuth2SuccessHandler, JwtFilter jwtFilter) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.OAuth2SuccessHandler = OAuth2SuccessHandler;
        this.jwtFilter = jwtFilter;
    }

    @Value("${frontend_url}")
    private String frontendUrl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors
                .configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    //config.addAllowedOrigin("http://192.168.0.109:5173"); // Frontend URL
                    config.addAllowedOrigin("http://localhost:5173"); // Frontend URL
                    //Production Enviorment
                    config.addAllowedOrigin("https://269593.kieg.science");

                    config.addAllowedMethod("*"); // Allow all HTTP methods
                    config.addAllowedHeader("*"); // Allow all headers
                    config.setAllowCredentials(true); // Allow cookies/auth tokens
                    return config;
                })
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/", "/swagger-ui/**", "/v3/api-docs/**", "/auth/**", "/login"
                    ).permitAll()
                .anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2
                .loginPage(frontendUrl + "/login")
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService))
                .successHandler(OAuth2SuccessHandler)
                // .defaultSuccessUrl(frontendUrl + "/#/dashboard", true)
                )
            .logout(logout -> logout
                .logoutSuccessUrl(frontendUrl + "/login")
                .permitAll())
            // .csrf(c -> c
            //     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            // )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            ;
        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

