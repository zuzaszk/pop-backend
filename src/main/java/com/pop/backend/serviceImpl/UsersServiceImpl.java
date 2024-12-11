package com.pop.backend.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.mapper.UserRoleMapper;
import com.pop.backend.mapper.UsersMapper;
import com.pop.backend.service.IUsersService;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    private final Map<Integer, Integer> currentRoleMap = new HashMap<>();
    
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
    public void setCurrentRoleForUser(Integer userId, Integer roleId) {
        currentRoleMap.put(userId, roleId);
    }

    @Override
    public Integer getCurrentRoleForUser(Integer userId) {
        return currentRoleMap.get(userId);
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

    @Override
    public Optional<Users> findByUsosId(String id) {
        return Optional.ofNullable(usersMapper.findByUsosId(id).orElse(null));
    }
    
}
