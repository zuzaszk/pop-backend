package com.pop.backend.controller;

import com.pop.backend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/invitations")
public class InvitationsController {



    @Autowired
    private EmailService emailService;





    @GetMapping("/sendProclamation")
    @Operation(
            summary = "A Divine, Spiritual and Exclusive API, dedicate to The One and The Only :)",
            description = "Author: YL"
    )
    public String sendProclamation() {
        String toEmail = "bamaliu@protonmail.com";
        //String toEmail = "269614@student.pwr.edu.pl";
        emailService.sendProclamationEmail(toEmail);
        return "Proclamation email sent to " + toEmail;
    }





}
