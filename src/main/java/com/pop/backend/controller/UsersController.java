package com.pop.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/user")
public class UsersController {
    
    @Autowired
    IUsersService usersService;

    @GetMapping("/listAll")
    public ResponseEntity<List<Users>> listAll() {
        List<Users> users = usersService.listAll();
        return ResponseEntity.ok(users);
    }

}