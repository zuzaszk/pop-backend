package com.pop.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pop.backend.auth.CustomUserDetails;
import com.pop.backend.mapper.UserRoleMapper;

@Service
public class AccessControlService {

    @Autowired UserRoleMapper userRoleMapper;

    public boolean isTeamMember(Integer projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();

        return userRoleMapper.isUserInProject(userId, projectId) > 0;
    }

}
