package com.pop.backend.serviceImpl;

import com.pop.backend.auth.TokenService;
import com.pop.backend.entity.Invitations;
import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.mapper.InvitationsMapper;
import com.pop.backend.mapper.UserRoleMapper;
import com.pop.backend.mapper.UsersMapper;
import com.pop.backend.service.EmailService;
import com.pop.backend.service.IInvitationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InvitationsServiceImpl extends ServiceImpl<InvitationsMapper, Invitations> implements IInvitationsService {

    @Autowired
    private InvitationsMapper invitationMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IUsersService userService;

    @Value("${frontend_url}")
    private String frontendUrl;

//    TODO: clean - move some logic to outside functions
    @Override
    public Invitations sendInvitation(String emailAddress, String roleName, Integer projectId, Integer editionId) {
        Invitations invitation = new Invitations();
        invitation.setEmailAddress(emailAddress);
        invitation.setRoleName(roleName);
        invitation.setState(0); // Initial state: 0 = Pending
        invitation.setCreatedAt(LocalDateTime.now());
        // TODO: Logic for when the invitation expires: move to archived && change state to expired (State: 3 = expired)
        invitation.setExpirationDate(LocalDateTime.now().plusDays(14)); // 14 days validity by default
        invitation.setIsArchived(false);

        // TODO: Token validation !
        String token = tokenService.generateInvitationToken(emailAddress, roleName, projectId, editionId);
        String invitationLink = frontendUrl + "/#/register?token=" + token;
        System.out.println("Invitation link: " + invitationLink);
        invitation.setInvitationLink(invitationLink);

        invitationMapper.insert(invitation);

//        TODO!:
//        org.springframework.mail.MailSendException: Mail server connection failed. Failed messages: jakarta.mail.MessagingException: Could not convert socket to TLS;
//        nested exception is:
//        javax.net.ssl.SSLHandshakeException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target; message exception details (1) are:
//        Failed message 1:
//        jakarta.mail.MessagingException: Could not convert socket to TLS;

        emailService.sendEmail(emailAddress, "Registration link",
                "Welcome to PoP! You’ve been invited to join. Please register using this link:\n" + invitationLink);
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

////    TODO: move to user
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
