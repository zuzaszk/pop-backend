package com.pop.backend.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.entity.Users;
import com.pop.backend.mapper.UsersMapper;
import com.pop.backend.service.IUsersService;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    
    @Autowired
    private UsersMapper usersMapper;
    
    @Override
    public Optional<Users> findByEmail(String email) {
        return Optional.ofNullable(usersMapper.findByEmail(email).orElse(null));
    }

    @Transactional
    @Override
    public void registerOAuthUser(Users user) {
        usersMapper.insert(user);
    }

    @Override
    public List<Users> listAll() {
        return usersMapper.selectList(null);
    }
    
}
