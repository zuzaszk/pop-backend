package com.pop.backend.controller;

import com.pop.backend.entity.Invitations;
import com.pop.backend.service.EmailService;
import com.pop.backend.service.IInvitationsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@RestController
@RequestMapping("/invitation")
public class InvitationsController {

    @Autowired
    private final IInvitationsService invitationsService;

    public InvitationsController(IInvitationsService invitationsService) {
        this.invitationsService = invitationsService;
    }

//    Role name like "spectator", "student"
    @PostMapping("/send")
    public ResponseEntity<Invitations> sendInvitation(
            @RequestParam String email,
            @RequestParam(required = false) String roleName,
            @RequestParam Integer projectId,
            @RequestParam Integer editionId
            ) {
        Invitations invitation = invitationsService.sendInvitation(email, roleName, projectId, editionId);
        return ResponseEntity.ok(invitation);
    }

//    After registration
//    @PostMapping("/accept")
//    public ResponseEntity<String> acceptInvitation(@RequestParam Integer invitationId) {
//        return invitationsService.acceptInvitation(invitationId) ?
//                ResponseEntity.ok("Invitation accepted.") :
//                ResponseEntity.badRequest().body("Failed to accept invitation.");
//    }

    @GetMapping("/id")
    public ResponseEntity<Integer> getInvitationId(@RequestParam String invitationLink) {
        return ResponseEntity.ok(invitationsService.findInvitationByInvitationLink(invitationLink).getInvitationId());
    }

}
