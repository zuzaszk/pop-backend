package com.pop.backend.auth;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pop.backend.entity.UserRole;
import com.pop.backend.service.IUsersService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    private final TokenService tokenService;
    private final IUsersService usersService;
    
    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer "
        try {
            var claims = tokenService.validateToken(token);
            String email = claims.getSubject(); // Extract user email
            Integer role = (Integer) claims.get("role", Integer.class);
            Integer id = (Integer) claims.get("id", Integer.class);
            List<UserRole> userRoles = usersService.findByEmailWithRole(email).get().getUserRole();

            CustomUserDetails userDetails = new CustomUserDetails(id, email, role, userRoles);

            var authentication = new UsernamePasswordAuthenticationToken(
               userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
    
}