package com.pop.backend.controller;

import com.pop.backend.entity.Invitations;
import com.pop.backend.service.EmailService;
import com.pop.backend.service.IInvitationsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

//TODO
@RestController
@RequestMapping("/invitation")
public class InvitationsController {

    @Autowired
    private final IInvitationsService invitationsService;

    public InvitationsController(IInvitationsService invitationsService) {
        this.invitationsService = invitationsService;
    }

    @PostMapping("/send")
    public ResponseEntity<Invitations> sendInvitation(
            @RequestParam String email,
            @RequestParam(required = false) String roleName
            ) {
        Invitations invitation = invitationsService.sendInvitation(email, roleName);
        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptInvitation(@RequestParam Integer invitationId) {
        return invitationsService.acceptInvitation(invitationId) ?
                ResponseEntity.ok("Invitation accepted.") :
                ResponseEntity.badRequest().body("Failed to accept invitation.");
    }

}
