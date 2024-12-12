package com.pop.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pop.backend.auth.RegistrationRequest;
import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;

public interface IUsersService extends IService<Users> {

    Optional<Users> findByEmail(String email);

    Users createUserFromRequest(RegistrationRequest request);

    Users createOAuth2User(OAuth2User oAuth2User);

    UserRole assignRoleToUser(Integer userId, Integer roleId, Integer editionId, Integer projectId);

    Optional<Users> findByEmailWithRole(String email);

    Integer findMaxUserId();

    void registerUser(Users user);

    void updateUser(Users user);

    void insertUserRole(UserRole userRole);

    List<UserRole> findUserRoles(Integer userId);

    List<Users> listAll();

    Users getBasicUserInfoById(Integer userId);

    void deleteUserRole(Integer userId, Integer roleId, Integer projectId, Integer editionId);

    void updateUserRole(Integer userId, Integer roleId, Integer projectId, Integer editionId, Integer newRoleId);

    Optional<Users> findByUsosId(String id);
}
