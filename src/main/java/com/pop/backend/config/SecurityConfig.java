package com.pop.backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        //for testing now, allow all requests
                        .anyRequest().permitAll()
                );

        return http.build();
    }


    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    // .allowedOrigins("http://localhost:5173")
                    .allowedOrigins("*")
                    //   .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    //   .allowCredentials(false);
                    .allowCredentials(false);
        }


    }

}
