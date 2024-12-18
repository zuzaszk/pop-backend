package com.pop.backend.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.pop.backend.security.CustomOAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String token = customOAuth2User.getJwtToken();

        // Send token in the response
        // response.setContentType("application/json");
        // response.getWriter().write("{\"token\": \"" + token + "\"}");

        // Redirect to frontend with token in the URL
        response.sendRedirect("https://269593.kieg.science/#/login-success?token=" + token);
    }
}
