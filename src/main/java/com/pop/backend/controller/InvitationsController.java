package com.pop.backend.controller;

import com.pop.backend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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










}
