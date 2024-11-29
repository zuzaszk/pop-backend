package com.pop.backend.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pop.backend.entity.UserRole;

@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
    // void insertUserRole(UserRole userRole);
    List<UserRole> findRolesByUserId(Integer userId);
    Integer findMaxUserRoleId();
    UserRole findRole(Integer userId, Integer roleId, Integer projectId, Integer editionId);
}
