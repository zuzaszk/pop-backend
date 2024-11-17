package com.pop.backend.serviceImpl;

import java.util.List;
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
    
    @Override
    public Optional<Users> findByEmail(String email) {
        return Optional.ofNullable(usersMapper.findByEmail(email).orElse(null));
    }

    @Transactional
    @Override
    public void registerUser(Users user) {
        usersMapper.insert(user);
    }

    @Override
    public List<Users> listAll() {
        return usersMapper.selectList(null);
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
    public List<UserRole> findUserRoles(Integer userId) {
       return userRoleMapper.findRolesByUserId(userId);
    }


    @Override
    public Users getBasicUserInfoById(Integer userId) {
        Users user = usersMapper.getBasicUserInfoById(userId);
        return user;
    }


    
}
