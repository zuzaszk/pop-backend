package com.pop.backend.controller;

import java.sql.Timestamp;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UsersController {
    
    @Autowired
    IUsersService usersService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/currentUser")
    @Operation(
            summary = "Get current user",
            tags = {"User"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Unauthorized");
        }

        String email = authentication.getName(); // Retrieve email from SecurityContext
        Users currentUser = usersService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.setUserRole(usersService.findUserRoles(currentUser.getUserId()));

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/findUser")
    @Operation(
            summary = "Find user by email",
            tags = {"User"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<?> findUser(@RequestParam String email) {
        Users user = usersService.findByEmail(email).orElse(null);
        return ResponseEntity.ok(user);
    }
    

    
    @PostMapping("/updateUser")
    @Operation(
            summary = "Update user information",
            tags = {"User"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> updateUser(@RequestParam Integer userId, @RequestBody Users updatedUser) {
        Users existingUser = usersService.getBasicUserInfoById(userId);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("User not found.");
        }

        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getAlternativeEmail() != null) {
            existingUser.setAlternativeEmail(updatedUser.getAlternativeEmail());
        }
        if (updatedUser.getKeywords() != null) {
            existingUser.setKeywords(updatedUser.getKeywords());
        }
        if (updatedUser.getPassword() != null || updatedUser.getAlternativeEmail() != null || updatedUser.getKeywords() != null) {
            existingUser.setLastLoginAt(new Timestamp(System.currentTimeMillis()));
        }
    
        usersService.updateUser(existingUser);
        return ResponseEntity.ok("User updated successfully.");
    }

    @DeleteMapping("/deleteUser")
    @Operation(
            summary = "Delete user",
            tags = {"User"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> deleteUser(@RequestParam Integer userId) {
        Users user = usersService.getBasicUserInfoById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("User not found.");
        }
        usersService.removeById(userId);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/listAll")
    @Operation(
            summary = "List all users",
            tags = {"User"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    @PreAuthorize("hasRole('ROLE_CHAIR')")
    public ResponseEntity<List<Users>> listAll() {
        List<Users> users = usersService.listAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/basicInfo")
    @Operation(
            summary = "Get basic user information",
            tags = {"User"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Users> getBasicUserInfoById(@RequestParam Integer userId) {
        Users user = usersService.getBasicUserInfoById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/switchRole")
    @Operation(
            summary = "Switch user role",
            tags = {"User", "Role"}
    )
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
    @Operation(
            summary = "Get current role for user",
            tags = {"User", "Role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Integer> getCurrentRole(@RequestParam Integer userId) {
        Integer currentRole = usersService.getCurrentRoleForUser(userId);
        if (currentRole == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(currentRole);
    }

    @PostMapping("/addRole")
    @Operation(
            summary = "Add role to user",
            tags = {"User", "Role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> addRole(
        @RequestParam("userId") Integer userId,
        @RequestParam("roleId") Integer roleId,
        @RequestParam(value = "projectId", required = false) Integer projectId,
        @RequestParam(value = "editionId", required = false) Integer editionId) {

            List<UserRole> userRoles = usersService.findUserRoles(userId);

            boolean hasRole = userRoles.stream().anyMatch(role -> role.getRoleId().equals(roleId)
                    && role.getProjectId().equals(projectId) && role.getEditionId().equals(editionId));

            if (hasRole) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("The user already has the specified role.");
            }

            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setProjectId(projectId);
            userRole.setEditionId(editionId);
            usersService.insertUserRole(userRole);

            return ResponseEntity.ok("Role added successfully.");
    }

    @PostMapping("/editRole")
    @Operation(
            summary = "Edit role of user",
            tags = {"User", "Role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> editRole(
        @RequestParam Integer userId,
        @RequestParam Integer roleId,
        @RequestParam Integer projectId,
        @RequestParam Integer editionId,
        @RequestParam Integer newRoleId) {

            List<UserRole> userRoles = usersService.findUserRoles(userId);

            boolean hasRole = userRoles.stream().anyMatch(role -> role.getRoleId().equals(roleId)
                    && role.getProjectId().equals(projectId) && role.getEditionId().equals(editionId));

            if (!hasRole) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("The user does not have the specified role.");
            }

            usersService.updateUserRole(userId, roleId, projectId, editionId, newRoleId);
            return ResponseEntity.ok("Role updated successfully.");
    }

    @DeleteMapping("/deleteRole")
    @Operation(
            summary = "Delete role from user",
            tags = {"User", "Role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> deleteRole(
        @RequestParam Integer userId,
        @RequestParam Integer roleId,
        @RequestParam(required = false) Integer projectId,
        @RequestParam(required = false) Integer editionId) {

            List<UserRole> userRoles = usersService.findUserRoles(userId);

            if (userRoles.size() == 1) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Cannot delete the last role of the user.");
            }

            boolean hasRole = userRoles.stream().anyMatch(role -> role.getRoleId().equals(roleId));
            if (!hasRole) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("The user does not have the specified role.");
            }

            usersService.deleteUserRole(userId, roleId, projectId, editionId);
            return ResponseEntity.ok("Role deleted successfully.");
    }

}
