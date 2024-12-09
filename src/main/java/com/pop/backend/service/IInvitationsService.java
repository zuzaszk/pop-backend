package com.pop.backend.service;

import com.pop.backend.entity.Invitations;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IInvitationsService extends IService<Invitations> {

//    TODO: Add projectId to the whole flow
    /**
     * Sends an invitation to the specified email address.
     *
     * @param emailAddress The recipient's email address.
     * @param roleName The role to be assigned to the new user.
     * @return The created invitation.
     */
    Invitations sendInvitation(String emailAddress, String roleName);

    /**
     * Marks an invitation as accepted.
     *
     * @param invitationId The ID of the invitation to accept.
     * @return True if the invitation was successfully updated, false otherwise.
     */
    boolean acceptInvitation(Integer invitationId);

    /**
     * Marks an invitation as declined.
     *
     * @param invitationId The ID of the invitation to decline.
     * @return True if the invitation was successfully updated, false otherwise.
     */
    boolean declineInvitation(Integer invitationId);

    /**
     * Archives an invitation, marking it as no longer active.
     *
     * @param invitationId The ID of the invitation to archive.
     * @return True if the invitation was successfully archived, false otherwise.
     */
    boolean archiveInvitation(Integer invitationId);

    /**
     * Checks if an invitation is expired.
     *
     * @param invitationId The ID of the invitation to check.
     * @return True if the invitation is expired, false otherwise.
     */
    boolean isInvitationExpired(Integer invitationId);

}
