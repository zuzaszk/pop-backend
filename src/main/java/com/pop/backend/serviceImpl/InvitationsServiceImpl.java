package com.pop.backend.serviceImpl;

import com.pop.backend.auth.TokenService;
import com.pop.backend.entity.Invitations;
import com.pop.backend.entity.Projects;
import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.mapper.InvitationsMapper;
import com.pop.backend.mapper.ProjectsMapper;
import com.pop.backend.service.EmailService;
import com.pop.backend.service.IInvitationsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.service.IUsersService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private ProjectsMapper projectsMapper;

    private static final Map<Integer, String> roleMap = new HashMap<>();
    static {
        roleMap.put(1, "student");
        roleMap.put(2, "supervisor");
        roleMap.put(3, "reviewer");
        roleMap.put(4, "chair");
        roleMap.put(5, "spectator");
    }

    public int getRoleIdByName(String roleName) {
        for (Map.Entry<Integer, String> entry : roleMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(roleName)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    @Value("${backend_url}")
    private String backendUrl;

    @Value("${frontend_url}")
    private String frontendUrl;

    @Override
    public Invitations sendInvitation(String emailAddress, String roleName, Integer projectId) {
        Invitations invitation = new Invitations();
        invitation.setEmailAddress(emailAddress);
        invitation.setState(0); // Initial state: 0 = Pending
        invitation.setCreatedAt(LocalDateTime.now());
        invitation.setExpirationDate(LocalDateTime.now().plusDays(14)); // 14 days validity by default
        invitation.setIsArchived(false);
        String token = tokenService.generateInvitationToken(emailAddress, roleName, projectId);
        String invitationLink = backendUrl + "/invitation/accept?token=" + token;
        invitation.setInvitationLink(invitationLink);
        invitationMapper.insert(invitation);
        if (!userService.findByEmail(emailAddress).isPresent()) {
            sendRegistrationMail(emailAddress, invitationLink);
        } else {
            sendInvitationMail(emailAddress, invitationLink);
        }
        return invitation;
    }

    public void sendRegistrationMail(String emailAddress, String invitationLink) {
        String registrationLink = frontendUrl + "/login";

        String subject = "Invitation to PoP";
        String htmlMessage = "<p>Welcome to PoP!</p>" +
                "<p>You've been invited to register and join a project group in PoP!</p>" +
                "<p>Please follow these steps to join:</p>" +
                "<ol>" +
                "<li><b>First, register your account:</b></li>" +
                "</ol>" +
                "<a href=\"" + registrationLink + "\" style=\"display: inline-block; " +
                "padding: 15px 30px; font-size: 18px; font-weight: bold; background-color: #007BFF; " +
                "color: white; text-decoration: none; border-radius: 8px;\">Register</a>" +
                "<p><i>(After registering, you'll be able to accept your invitation)</i></p>" +
                "<ol start=\"2\">" +
                "<li><b>Then, click the button below to accept the invitation:</b></li>" +
                "</ol>" +
                "<a href=\"" + invitationLink + "\" style=\"display: inline-block; " +
                "padding: 10px 20px; font-size: 16px; background-color: #4CAF50; " +
                "color: white; text-decoration: none; border-radius: 5px;\">Accept Invitation</a>" +
                "<p>If any of the buttons don't work, you can copy and paste these links into your browser:</p>" +
                "<p><b>Register:</b> <a href=\"" + registrationLink + "\">" + registrationLink + "</a></p>" +
                "<p><b>Accept Invitation:</b> <a href=\"" + invitationLink + "\">" + invitationLink + "</a></p>";
        emailService.sendEmailHTML(emailAddress, subject, htmlMessage);
    }

    public void sendInvitationMail(String emailAddress, String invitationLink) {
        String subject = "Invitation to Join a Project";
        String htmlMessage = "<p>You have been invited to join a project!</p>" +
                "<p>You've been invited to join a project group in PoP! Please click the button below to accept the invitation:</p>" +
                "<a href=\"" + invitationLink + "\" style=\"display: inline-block; " +
                "padding: 10px 20px; background-color: #4CAF50; color: white; " +
                "text-decoration: none; border-radius: 5px;\">Accept Invitation</a>" +
                "<p>If the button doesn't work, copy and paste this link into your browser:</p>" +
                "<p><a href=\"" + invitationLink + "\">" + invitationLink + "</a></p>";

        emailService.sendEmailHTML(emailAddress, subject, htmlMessage);
    }

    @Override
    public String acceptInvitation(String token) {
        Claims claims = tokenService.validateToken(token);

        String email = claims.getSubject();
        String roleName = claims.get("role_name", String.class);
        Integer projectId = claims.get("project_id", Integer.class);

        Optional<Users> user = userService.findByEmailWithRole(email);

        if (!user.isPresent()) {
            return frontendUrl + ""; // TODO: link for register first
        }

        Invitations invitation = findInvitationByInvitationLink(backendUrl + "/invitation/accept?token=" + token);
        if (invitation == null || invitation.getIsArchived() || isInvitationExpired(invitation.getInvitationId())) {
            return null;
        }

        invitation.setState(1); // State: 1 = Accepted
        archiveInvitation(invitation);

        processInvitationAcceptance(invitation, user, roleName, projectId);

        return frontendUrl + ""; // TODO: link for invitation accepted
    }

    private void processInvitationAcceptance(Invitations invitation, Optional<Users> user, String roleName, Integer projectId) {
        Integer userId = user.map(Users::getUserId).orElse(null);
        invitation.setUserId(userId);

        Integer roleId = getRoleIdByName(roleName);
        Projects project = projectsMapper.getProjectWithUsersAndEditionById(projectId);
        Integer editionId = project.getEditionId();

        UserRole userRole = createUserRole(userId, roleId, projectId, editionId);
        userService.insertUserRole(userRole);

        Integer userRoleId = userRole.getUserRoleId();
        invitation.setUserRoleId(userRoleId);
        updateInvitation(invitation);
    }

    private UserRole createUserRole(Integer userId, Integer roleId, Integer projectId, Integer editionId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setProjectId(projectId);
        userRole.setEditionId(editionId);

        return userRole;
    }

    @Override
    public boolean archiveInvitation(Invitations invitation) {
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

    @Override
    public Invitations findInvitationByInvitationLink(String invitationLink) {
        return invitationMapper.selectOne(new QueryWrapper<Invitations>().eq("invitation_link", invitationLink));
    }

    @Override
    public List<Invitations> listAll(Integer userId, Boolean isForCurrentUser) {
        return invitationMapper.getAllInvitations(userId, isForCurrentUser);
    }

    @Override
    public void updateInvitation(Invitations invitation) {
        invitationMapper.updateById(invitation);
    }

}
