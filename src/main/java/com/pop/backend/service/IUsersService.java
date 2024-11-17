package com.pop.backend.service;

import java.util.List;
import java.util.Optional;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pop.backend.entity.Users;

public interface IUsersService extends IService<Users> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByEmailWithRole(String email);

    Integer findMaxUserId();

    void registerOAuthUser(Users user);

    List<Users> listAll();
}
