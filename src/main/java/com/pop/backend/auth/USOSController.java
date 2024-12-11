package com.pop.backend.auth;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/usos")
public class USOSController {
    
    @Autowired
    private OAuth10aService oauthService;

    @Autowired
    private IUsersService userService;

    @Autowired
    private TokenService tokenService;

    @Value("${usos.api.base.url}")
    private String usosApiBaseUrl;

    @GetMapping("/login")
    @Operation(
            summary = "Login using USOS OAuth 1.0",
            tags = {"Auth", "USOS"}
    )
    public ResponseEntity<AuthResponse> usosLogin(HttpSession session) throws Exception {
        OAuth1RequestToken requestToken = oauthService.getRequestToken();
        session.setAttribute("requestToken", requestToken);
        String authorizationUrl = oauthService.getAuthorizationUrl(requestToken);
        return ResponseEntity.ok(new AuthResponse("Redirecting to: " + authorizationUrl, authorizationUrl));
    }

    
    @GetMapping("/callback")
    @Operation(
        summary = "Callback for USOS OAuth 1.0",
        tags = {"Auth", "USOS"}
    )
    public ResponseEntity<Void> usosCallback(
        @RequestParam String oauth_verifier,
        HttpSession session) throws Exception {
        
        OAuth1RequestToken requestToken = (OAuth1RequestToken) session.getAttribute("requestToken");
        if (requestToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        OAuth1AccessToken accessToken = oauthService.getAccessToken(requestToken, oauth_verifier);
        OAuthRequest request = new OAuthRequest(Verb.GET, usosApiBaseUrl + "/users/user");
        request.addQuerystringParameter("fields", "id|first_name|last_name|student_status");

        oauthService.signRequest(accessToken, request);
        String responseBody = oauthService.execute(request).getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userInfo = objectMapper.readValue(responseBody, new TypeReference<>() {});

        String id = (String) userInfo.get("id");
        String firstName = (String) userInfo.get("first_name");
        String lastName = (String) userInfo.get("last_name");
        Integer studentStatus = (Integer) userInfo.get("student_status");

        if (userService.findByUsosId(id).isPresent()) {
            Users user = userService.findByUsosId(id).get();
            String loginToken = tokenService.generateToken(user, user.getUserRole().get(0).getRoleId());
            return ResponseEntity.ok().header("Authorization", loginToken).build();
        }

        String token = tokenService.generateTemporaryToken(Map.of(
            "id", id,
            "firstName", firstName,
            "lastName", lastName,
            "studentStatus", studentStatus
        ));

        String frontendUrl = "http://localhost:5173?token=" + token;
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl)).build();
    }

    @PostMapping("/finalize-user")
    @Operation(
        summary = "Finalize user registration or update",
        tags = {"Auth", "USOS"}
    )
    public ResponseEntity<AuthResponse> finalizeUser(
        @RequestParam String token, @RequestParam String email) {
   
        Integer currentRole;

        if (token == null || email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Missing token or email", null));
        }

        Map<String, Object> userInfo = tokenService.validateToken(token);

        String id = (String) userInfo.get("id");
        String firstName = (String) userInfo.get("firstName");
        String lastName = (String) userInfo.get("lastName");
        Integer studentStatus = (Integer) userInfo.get("studentStatus");

        Users user;
        if (userService.findByEmail(email).isPresent()) {
            user = userService.findByEmail(email).get();
            user.setUsosId(id);
            user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));
            userService.updateUser(user);
        } else {
            user = new Users();
            user.setUsosId(id);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));
            userService.registerUser(user);
        }

        if (studentStatus == 2) {
            UserRole studentRole = new UserRole();
            studentRole.setUserId(user.getUserId());
            studentRole.setRoleId(1);
            userService.insertUserRole(studentRole);
            currentRole = 1;
        } else {
            currentRole = 5;
        }
        UserRole spectatorRole = new UserRole();
        spectatorRole.setUserId(user.getUserId());
        spectatorRole.setRoleId(5);
        userService.insertUserRole(spectatorRole);

        String loginToken = tokenService.generateToken(user, currentRole);

        return ResponseEntity.ok(new AuthResponse("User registered/updated successfully", loginToken));
    }


}