package main.java.com.pop.backend.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${frontend_url}")
    private String frontendUrl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/login").permitAll()
                .anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2
                .loginPage(frontendUrl + "/login")
                // .failureUrl("/login?error")
                // .defaultSuccessUrl(frontendUrl + "/dashboard", true)
                )
            .logout(logout -> logout
                .logoutSuccessUrl(frontendUrl + "/login")
                .permitAll());
        return http.build();
    }

}
