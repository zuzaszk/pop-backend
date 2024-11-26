package com.pop.backend.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/user")
public class UsersController {
    
    @Autowired
    IUsersService usersService;


    @GetMapping("/currentUser")
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Unauthorized");
        }

        String email = authentication.getName(); // Retrieve email from SecurityContext
        Users currentUser = usersService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // // Exclude sensitive information like password
        // currentUser.setPassword(null);

        currentUser.setUserRole(usersService.findUserRoles(currentUser.getUserId()));

        return ResponseEntity.ok(currentUser);
    }

    
    @GetMapping("/listAll")
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    @PreAuthorize("hasRole('ROLE_CHAIR')")
    public ResponseEntity<List<Users>> listAll() {
        List<Users> users = usersService.listAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/basicInfo")
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Users> getBasicUserInfoById(@RequestParam Integer userId) {
        Users user = usersService.getBasicUserInfoById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/switchRole")
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> switchRole(@RequestParam Integer userId, @RequestParam Integer roleId) {
        List<UserRole> userRoles = usersService.findUserRoles(userId);

        boolean hasRole = userRoles.stream().anyMatch(
            role -> role.getRoleId().equals(roleId)
        );

        if (!hasRole) {
            return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body("The user does not have the specified role.");
        }

        usersService.setCurrentRoleForUser(userId, roleId);
        return ResponseEntity.ok("Switched to role: " + roleId);
    }

    @GetMapping("/currentRole")
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Integer> getCurrentRole(@RequestParam Integer userId) {
        Integer currentRole = usersService.getCurrentRoleForUser(userId);
        if (currentRole == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(currentRole);
    }
}
