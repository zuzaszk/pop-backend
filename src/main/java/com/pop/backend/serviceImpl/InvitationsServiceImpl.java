package com.pop.backend.serviceImpl;

import com.pop.backend.entity.Invitations;
import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.mapper.InvitationsMapper;
import com.pop.backend.mapper.UserRoleMapper;
import com.pop.backend.mapper.UsersMapper;
import com.pop.backend.service.IInvitationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// TODO
@Service
public class InvitationsServiceImpl extends ServiceImpl<InvitationsMapper, Invitations> implements IInvitationsService {

    @Autowired
    private InvitationsMapper invitationMapper;

    @Autowired
    private IUsersService userService;

    // TODO: param projectId ?
    @Override
    public Invitations sendInvitation(String emailAddress, String roleName) {
        Invitations invitation = new Invitations();
        invitation.setEmailAddress(emailAddress);
        invitation.setRoleName(roleName);
        invitation.setState(0); // Initial state: 0 = Pending
        invitation.setCreatedAt(LocalDateTime.now());
        // TODO: Logic for when the invitation expires: move to archived && change state to expired (State: 3 = expired)
        invitation.setExpirationDate(LocalDateTime.now().plusDays(14)); // 14 days validity by default
        invitation.setIsArchived(false);

        // TODO: Generate an invitation link (mock example, replace with actual generation logic)
        invitation.setInvitationLink("mocklink123");

        invitationMapper.insert(invitation);
        return invitation;
    }

    @Override
    public boolean acceptInvitation(Integer invitationId) {
        Invitations invitation = invitationMapper.selectById(invitationId);
        if (invitation == null || invitation.getIsArchived() || isInvitationExpired(invitationId)) {
            return false;
        }
        invitation.setState(1); // State: 1 = Accepted
        return invitationMapper.updateById(invitation) > 0; // returns number of rows affected: 0 or 1 (false or true)
    }

    @Override
    public boolean declineInvitation(Integer invitationId) {
        Invitations invitation = invitationMapper.selectById(invitationId);
        if (invitation == null || invitation.getIsArchived() || isInvitationExpired(invitationId)) {
            return false;
        }
        invitation.setState(2); // State: 2 = Declined
        return invitationMapper.updateById(invitation) > 0;
    }

    @Override
    public boolean archiveInvitation(Integer invitationId) {
        Invitations invitation = invitationMapper.selectById(invitationId);
        if (invitation == null) {
            return false;
        }
        invitation.setIsArchived(true);
        return invitationMapper.updateById(invitation) > 0;
    }

    @Override
    public boolean isInvitationExpired(Integer invitationId) {
        Invitations invitation = invitationMapper.selectById(invitationId);
        return invitation != null && invitation.getExpirationDate().isBefore(LocalDateTime.now());
    }

////    TODO
//    @Override
//    @Transactional
//    public void updateUserRole(int userId, int newRoleId) {
//        Users user = userMapper.selectById(userId);
//        if (user == null) {
//            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
//        }
//
//        UserRole role = userRoleMapper.selectById(newRoleId);
//        if (role == null) {
//            throw new IllegalArgumentException("Role with ID " + newRoleId + " does not exist.");
//        }
//
////        TODO: fucked up parameters
////        user.updateUserRole(userId, newRoleId);
//        int rowsUpdated = userMapper.updateById(user);
//        if (rowsUpdated != 1) {
//            throw new RuntimeException("Failed to update user role for user ID " + userId);
//        }
//
//        System.out.println("Updated role for user ID " + userId + " to role ID " + newRoleId);
//    }

}
