package com.pop.backend.controller;

import com.pop.backend.auth.CustomUserDetails;
import com.pop.backend.auth.TokenService;
import com.pop.backend.entity.Invitations;
import com.pop.backend.service.IInvitationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/invitation")
public class InvitationsController {

    @Autowired
    private final IInvitationsService invitationsService;

    @Autowired
    private TokenService tokenService;

    public InvitationsController(IInvitationsService invitationsService) {
        this.invitationsService = invitationsService;
    }

//   Role name like "spectator", "student"
    @PostMapping("/send")
    public ResponseEntity<Invitations> sendInvitation(
            @RequestParam String email,
            @RequestParam(required = false) String roleName,
            @RequestParam Integer projectId
            ) {
        Invitations invitation = invitationsService.sendInvitation(email, roleName, projectId);
        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/accept")
    public ResponseEntity<Void> acceptInvitation(@RequestParam String token) {
        String redirectionLink = invitationsService.acceptInvitation(token);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectionLink)).build();
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Invitations>> listAll(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "isForCurrentUser", defaultValue = "false") Boolean isForCurrentUser) {
        Integer userId = userDetails.getUserId();
        List<Invitations> invitations = invitationsService.listAll(userId, isForCurrentUser);
        return ResponseEntity.ok(invitations);
    }

    @GetMapping("/id")
    public ResponseEntity<Integer> getInvitationId(@RequestParam String invitationLink) {
        return ResponseEntity.ok(invitationsService.findInvitationByInvitationLink(invitationLink).getInvitationId());
    }

}
