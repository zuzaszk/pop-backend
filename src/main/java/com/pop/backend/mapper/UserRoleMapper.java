package com.pop.backend.mapper;

import com.pop.backend.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-16
 */

@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<UserRole> getUserRelatedToProject(@Param("projectId") Integer projectId);


    /**
     * Get a user's role in a specific project.
     * @return the UserRole associated with the project, user, and role
     */
    UserRole getUserRoleForProject(
            @Param("projectId") Integer projectId,
            @Param("userId") Integer userId,
            @Param("roleId") Integer roleId
    );

    UserRole isTeachingMember(@Param("userId") Integer userId);

    int updateUserRole(@Param("projectId") Integer projectId,
                       @Param("userId") Integer userId,
                       @Param("newRoleId") Integer newRoleId);



    // void insertUserRole(UserRole userRole);
    List<UserRole> findRolesByUserId(Integer userId);
    Integer findMaxUserRoleId();
    UserRole findRole(Integer userId, Integer roleId, Integer projectId, Integer editionId);
}
