package com.pop.backend.serviceImpl;

import com.pop.backend.common.ApiResponse;
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

    @Override
    public ApiResponse<String> removeTeamMembers(Integer projectId, List<Integer> userIds) {
        // Validate each member
        for (Integer userId : userIds) {
            UserRole teamMemberRole = userRoleMapper.getUserRoleForProject(projectId, userId, 1); // Role ID for Student
            if (teamMemberRole == null) {
                throw new IllegalArgumentException("User with ID " + userId + " is not a team member of this project.");
            }

            int rowsUpdated = userRoleMapper.updateUserRole(projectId, userId, 5); // Role ID for Spectator
            if (rowsUpdated == 0) {
                throw new IllegalStateException("Failed to update role for user with ID " + userId + ". Please try again.");
            }
        }

        return new ApiResponse<>(true, "Team members removed successfully. The users are now spectators.", null);
    }
}
