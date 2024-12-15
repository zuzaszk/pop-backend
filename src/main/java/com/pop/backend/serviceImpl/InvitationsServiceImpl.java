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

    @Value("${frontend_url}")
    private String frontendUrl;

    @Override
    public Invitations sendInvitation(String emailAddress, String roleName, Integer projectId) {
        Invitations invitation = new Invitations();
        invitation.setEmailAddress(emailAddress);
        invitation.setState(0); // Initial state: 0 = Pending
        invitation.setCreatedAt(LocalDateTime.now());
        // TODO: Logic for when the invitation expires: move to archived && change state to expired
        invitation.setExpirationDate(LocalDateTime.now().plusDays(14)); // 14 days validity by default
        invitation.setIsArchived(false);
        String token = tokenService.generateInvitationToken(emailAddress, roleName, projectId);
        String invitationLink = frontendUrl + "/#/register?token=" + token;
        System.out.println("Invitation link: " + invitationLink);
        invitation.setInvitationLink(invitationLink);
        invitationMapper.insert(invitation);
        emailService.sendEmail(emailAddress, "Registration link",
                "Welcome to PoP! You’ve been invited to join. Please register using this link:\n" + invitationLink);
        return invitation;
    }

    @Override
    public boolean acceptInvitation(Integer invitationId) {
        if (invitationId == null) {
            return false;
        }
        Invitations invitation = invitationMapper.selectById(invitationId);
        if (invitation == null || invitation.getIsArchived() || isInvitationExpired(invitationId)) {
            return false;
        }
        invitation.setState(1); // State: 1 = Accepted
        archiveInvitation(invitation);
        String token = extractToken(invitation.getInvitationLink());
        Claims claims = tokenService.validateToken(token);
        String email = claims.getSubject();
        String roleName = claims.get("role_name", String.class);
        Integer projectId = claims.get("project_id", Integer.class);
        Optional<Users> user = userService.findByEmailWithRole(email);
        Integer userId = user.map(u -> u.getUserId()).orElse(null);
        invitation.setUserId(userId);
        Integer roleId = getRoleIdByName(roleName);
        Projects project = projectsMapper.getProjectWithUsersAndEditionById(projectId);
        Integer editionId = project.getEditionId();
        
        UserRole userRole = new UserRole();

        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setProjectId(projectId);
        userRole.setEditionId(editionId);

        userService.insertUserRole(userRole);

        Integer userRoleId = userRole.getUserRoleId();
//         Integer userRoleId = userService.updateUserRoleFull(userId, 5, roleId, projectId, editionId);
        invitation.setUserRoleId(userRoleId);
        return invitationMapper.updateById(invitation) > 0;
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

    public String extractToken(String url) {
        String tokenPrefix = "http://localhost:5173/#/register?token=";
        if (url.startsWith(tokenPrefix)) {
            return url.substring(tokenPrefix.length());
        }
        return null;
    }

    @Override
    public Invitations findInvitationByInvitationLink(String invitationLink) {
        return invitationMapper.selectOne(new QueryWrapper<Invitations>().eq("invitation_link", invitationLink));
    }

    @Override
    public List<Invitations> listAll() {
        return invitationMapper.getAllInvitations();
    }

}
