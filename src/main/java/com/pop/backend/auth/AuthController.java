package com.pop.backend.auth;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    // @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegistrationRequest request) {
        System.out.println("Register endpoint hit");
        System.out.println(request);

        try {
            if (usersService.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse("Email already exists!", null));
            }

            Users newUser = new Users();
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setName(request.getUsername());
            newUser.setFirstName(request.getFirstName());
            newUser.setLastName(request.getLastName());
            newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            newUser.setLastLoginAt(new Timestamp(System.currentTimeMillis()));

            try {
                usersService.registerUser(newUser);
            } catch (Exception e) {
                e.printStackTrace();
                Integer maxUserId = usersService.findMaxUserId();
                newUser.setUserId(maxUserId + 1);
                usersService.registerUser(newUser);
                System.out.println("User registered successfully!");
            }

            // return ResponseEntity.ok("User registered successfully!");

            String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new AuthResponse("User registered successfully!", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Internal server error", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        System.out.println("Login endpoint hit");
        System.out.println(request);
        
        try {
            Optional<Users> userOptional = usersService.findByEmail(request.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            Users user = userOptional.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse("User logged in successfully!", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Internal server error", null));
        }
    }
}

