package com.pop.backend.service;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-16
 */
public interface IUserRoleService extends IService<UserRole> {

    List<UserRole> getUserRelatedToProject(Integer projectId);

    ApiResponse<String> removeTeamMembers(Integer projectId, List<Integer> userIds);

}
