package com.pop.backend.serviceImpl;

import com.pop.backend.entity.UserRole;
import com.pop.backend.mapper.UserRoleMapper;
import com.pop.backend.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-16
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> getUserRelatedToProject(Integer projectId) {
        return userRoleMapper.getUserRelatedToProject(projectId);
    }
}
