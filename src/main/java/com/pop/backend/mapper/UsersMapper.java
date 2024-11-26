package com.pop.backend.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pop.backend.entity.Users;

@Repository
public interface UsersMapper extends BaseMapper<Users> {
    
    Optional<Users> findByEmail(@Param("email") String email);

    Optional<Users> findByEmailWithRole(@Param("email") String email);

    Integer findMaxUserId();

    Users getBasicUserInfoById(@Param ("userId") Integer userId);

    List<Users> listAllWithRoles();
}
