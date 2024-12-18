package com.pop.backend.serviceImpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.common.RegistrationRequest;
import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.mapper.RolesMapper;
import com.pop.backend.mapper.UserRoleMapper;
import com.pop.backend.mapper.UsersMapper;
import com.pop.backend.service.IUsersService;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public Optional<Users> findByEmail(String email) {
        return Optional.ofNullable(usersMapper.findByEmail(email).orElse(null));
    }

    @Override
    public Optional<Users> findByEmailWithRole(String email) {
        return Optional.ofNullable(usersMapper.findByEmailWithRole(email).orElse(null));
    }

    @Transactional
    @Override
    public void registerUser(Users user) {
        usersMapper.insert(user);
    }

    @Override
    public List<Users> listAll() {
        // return usersMapper.selectList(null);
        return usersMapper.listAllWithRoles();
    }

    @Override
    public Integer findMaxUserId() {
        return usersMapper.findMaxUserId();
    }

    @Override
    public void updateUser(Users user) {
        usersMapper.updateById(user);
    }

    @Override
    public void insertUserRole(UserRole userRole) {
        try {
            userRoleMapper.insert(userRole);
        } catch (Exception e) {
            // e.printStackTrace();
            userRole.setUserRoleId(userRoleMapper.findMaxUserRoleId() + 1);
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void deleteUserRole(Integer userId, Integer roleId, Integer projectId, Integer editionId) {
        UserRole userRole = userRoleMapper.findRole(userId, roleId, projectId, editionId);
        if (userRole != null) {
            userRoleMapper.deleteById(userRole.getUserRoleId());
        } else {
            throw new IllegalArgumentException("No matching user role found for deletion.");
        }
    }

    @Override
    public List<UserRole> findUserRoles(Integer userId) {
       return userRoleMapper.findRolesByUserId(userId);
    }


    @Override
    public Users getBasicUserInfoById(Integer userId) {
        Users user = usersMapper.getBasicUserInfoById(userId);
        return user;
    }

    @Override
    public void updateUserRole(Integer userId, Integer roleId, Integer projectId, Integer editionId,
            Integer newRoleId) {
        UserRole userRole = userRoleMapper.findRole(userId, roleId, projectId, editionId);
        if (userRole != null) {
            userRole.setRoleId(newRoleId);
            userRoleMapper.updateById(userRole);
        } else {
            throw new IllegalArgumentException("No matching user role found for update.");
        }
    }

    public Users createUserFromRequest(RegistrationRequest request) {
        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getFirstName() + " " + request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));
        return user;
    }

    @Override
    public Users createOAuth2User(OAuth2User oAuth2User) {
        Users user = new Users();

        user.setEmail(oAuth2User.getAttribute("email"));
        user.setName(oAuth2User.getAttribute("family_name") + " " + oAuth2User.getAttribute("given_name"));
        user.setFirstName(oAuth2User.getAttribute("given_name"));
        user.setLastName(oAuth2User.getAttribute("family_name"));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));

        return user;

    }

    @Override
    public Users createUSOSUser(Map<String, Object> userInfo, String email) {

        String id = (String) userInfo.get("id");
        String firstName = (String) userInfo.get("firstName");
        String lastName = (String) userInfo.get("lastName");
        String name = firstName + " " + lastName;

        Users user = new Users();

        user.setUsosId(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setName(name);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));

        return user;
    }

    @Override
    public UserRole assignRoleToUser(Integer userId, Integer roleId, Integer editionId, Integer projectId) {
        UserRole userRole = new UserRole();

        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setEditionId(editionId);
        userRole.setProjectId(projectId);
        userRole.setRoles(rolesMapper.selectById(roleId));

        userRoleMapper.insert(userRole);
        return userRole;
    }

    @Override
    public Optional<Users> findByUsosId(String id) {
        return Optional.ofNullable(usersMapper.findByUsosId(id).orElse(null));
    }

}
