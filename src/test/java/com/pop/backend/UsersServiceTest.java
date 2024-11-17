package com.pop.backend;

import com.pop.backend.entity.Users;
import com.pop.backend.serviceImpl.UsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UsersServiceTest {

    @Autowired
    private UsersServiceImpl usersService;

    @Test
    public void testFindByEmail() {
        String email = "266902@student.pwr.edu.pl";

        Optional<Users> userOptional = usersService.findByEmailWithRole(email);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            System.out.println("User found: " + user);
        } else {
            System.out.println("No user found with email: " + email);
        }
    }
}
