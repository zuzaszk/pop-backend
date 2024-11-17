package com.pop.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
//    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<List<Users>> listAll() {
        List<Users> users = usersService.listAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/basicInfo")
    public ResponseEntity<Users> getBasicUserInfoById(@RequestParam Integer userId) {
        Users user = usersService.getBasicUserInfoById(userId);
        return ResponseEntity.ok(user);
    }
}
