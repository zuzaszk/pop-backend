package com.pop.backend.controller;

import java.sql.Timestamp;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pop.backend.auth.TokenService;
import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;
import com.pop.backend.auth.CustomUserDetails;

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
    TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/currentUser")
    @Operation(
            summary = "Get current user",
            tags = {"User"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<?> getCurrentUser(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            String email = userDetails.getEmail();
            Users currentUser = usersService.findByEmailWithRole(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(currentUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Unauthorized");
        }

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
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, /*@RequestParam Integer userId,*/ @RequestBody Users updatedUser) {
        Integer userId = userDetails.getUserId();
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
            tags = {"User role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> switchRole(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        // @RequestParam Integer userId,
        @RequestParam Integer roleId
        ) {
            Integer userId = userDetails.getUserId();
            List<UserRole> userRoles = usersService.findUserRoles(userId);
            Users user = usersService.getById(userId);

            boolean hasRole = userRoles.stream().anyMatch(
                role -> role.getRoleId().equals(roleId)
            );

            if (!hasRole) {
                return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body("The user does not have the specified role.");
            }

            // usersService.setCurrentRoleForUser(userId, roleId);
            String newToken = tokenService.generateToken(user, roleId);
            return ResponseEntity.ok("Switched to role: " + roleId + "\nNew token: " + newToken);
    }


    @GetMapping("/currentRole")
    @Operation(
            summary = "Get current role for user",
            tags = {"User role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Integer> getCurrentRole(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            Integer currentRole = userDetails.getRole();
            return ResponseEntity.ok(currentRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(null);
        }
    }


    @PostMapping("/addRole")
    @Operation(
            summary = "Add role to user",
            tags = {"User role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> addRole(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(required = false) Integer userId,
        @RequestParam Integer roleId,
        @RequestParam(required = false) Integer projectId,
        @RequestParam(required = false) Integer editionId) {

            if (userId == null) {
                userId = userDetails.getUserId();
            }

            List<UserRole> userRoles = usersService.findUserRoles(userId);

            boolean hasRole = userRoles.stream().anyMatch(role -> role.getRoleId().equals(roleId)
                    && role.getProjectId().equals(projectId) && role.getEditionId().equals(editionId));

            if (hasRole) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("The user already has the specified role.");
            }

            usersService.assignRoleToUser(userId, roleId, editionId, projectId);

            return ResponseEntity.ok("Role added successfully.");
    }

    @PostMapping("/editRole")
    @Operation(
            summary = "Edit role of user",
            tags = {"User role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> editRole(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(required = false) Integer userId,
        @RequestParam Integer roleId,
        @RequestParam Integer projectId,
        @RequestParam Integer editionId,
        @RequestParam Integer newRoleId,
        @RequestParam(required = false) Integer newProjectId,
        @RequestParam(required = false) Integer newEditionId) {

            if (userId == null) {
                userId = userDetails.getUserId();
            }

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
            tags = {"User role"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> deleteRole(
        // @RequestHeader("Authorization") String authorizationHeader,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(required = false) Integer userId,
        @RequestParam Integer roleId,
        @RequestParam(required = false) Integer projectId,
        @RequestParam(required = false) Integer editionId) {

            if (userId == null) {
                userId = userDetails.getUserId();
            }
            Integer currentRole = userDetails.getRole();
            // String authorizationHeader = userDetails.getToken();
            // Integer currentRole = tokenService.getRoleFromToken(authorizationHeader.replace("Bearer ", ""));
            List<UserRole> userRoles = usersService.findUserRoles(userId);

            if (userRoles.size() == 1) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Cannot delete the last role of the user.");
            }

            boolean hasRole = userRoles.stream().anyMatch(role -> role.getRoleId().equals(roleId));
            if (!hasRole) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("The user does not have the specified role.");
            }

            String newToken = null;
            if (currentRole.equals(roleId)) {
                currentRole = userRoles.stream().filter(role -> !role.getRoleId().equals(roleId)).findFirst().get().getRoleId();
                newToken = tokenService.generateToken(usersService.getById(userId), currentRole);
            }

            usersService.deleteUserRole(userId, roleId, projectId, editionId);
            return ResponseEntity.ok("Role deleted successfully.\nNew token: " + newToken);
    }

}
