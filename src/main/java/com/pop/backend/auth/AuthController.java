package com.pop.backend.auth;

import java.sql.Timestamp;
import java.util.Optional;

import com.pop.backend.service.IInvitationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pop.backend.common.AuthResponse;
import com.pop.backend.common.ForgotPasswordRequest;
import com.pop.backend.common.LoginRequest;
import com.pop.backend.common.RegistrationRequest;
import com.pop.backend.common.ResetPasswordRequest;
import com.pop.backend.entity.Users;
import com.pop.backend.service.EmailService;
import com.pop.backend.service.IUsersService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IInvitationsService invitationsService;

    @Value("${frontend_url}")
    private String frontendUrl;

    // @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            tags = {"Authentication"}
    )
    public ResponseEntity<AuthResponse> register(@RequestBody RegistrationRequest request) {

        try {
            if (usersService.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse("Email already exists!", null));
            }

            Users newUser = usersService.createUserFromRequest(request);
            usersService.registerUser(newUser);
            usersService.assignRoleToUser(newUser.getUserId(), 5, null, null);


            String token = tokenService.generateToken(newUser, 5); // 5 (spectator) is the default role for new users

            invitationsService.acceptInvitation(request.getInvitationId());

            return ResponseEntity.ok(new AuthResponse("User registered successfully!", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Internal server error", null));
        }
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            tags = {"Authentication"}
    )
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        try {
            Optional<Users> userOptional = usersService.findByEmailWithRole(request.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            Users user = userOptional.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));

            Integer currentRole = user.getUserRole().get(0).getRoleId();
            usersService.updateUser(user);

            String token = tokenService.generateToken(user, currentRole);

            return ResponseEntity.ok(new AuthResponse("User logged in successfully!", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Internal server error", null));
        }
    }

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Send password reset link to user's email",
            tags = {"Authentication"}
    )
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            Optional<Users> userOptional = usersService.findByEmail(request.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Users user = userOptional.get();
            String token = tokenService.generateResetToken(user);
            String resetLink = frontendUrl + "/#/reset-password?token=" + token;
            
            emailService.sendEmail(request.getEmail(), "Password Reset Link", resetLink);

            return ResponseEntity.ok(new AuthResponse("Password reset link sent to your email!", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Internal server error", null));
        }
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset user's password",
            tags = {"Authentication"}
    )
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        System.out.println("Reset password endpoint hit");
        System.out.println(request);

        try {
            String email = tokenService.validateToken(request.getToken()).getSubject();
            Optional<Users> userOptional = usersService.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Users user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            usersService.updateUser(user);

            return ResponseEntity.ok(new AuthResponse("Password reset successfully!", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Invalid or expired token", null));
        }
    }
}

