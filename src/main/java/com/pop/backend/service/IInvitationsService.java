package com.pop.backend.service;

import com.pop.backend.entity.Invitations;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

public interface IInvitationsService extends IService<Invitations> {

    /**
     * Sends an invitation to the specified email address.
     *
     * This method creates a new invitation and sends it to the recipient via email.
     *
     * @param emailAddress The email address of the recipient who is being invited.
     * @param roleName The name of the role to assign to the recipient (e.g., "student", "supervisor").
     * @param projectId The ID of the project associated with the invitation (can be null if not applicable).
     * @return The created invitation.
     */
    Invitations sendInvitation(String emailAddress, String roleName, Integer projectId);

    /**
     * Accepts an invitation using the provided token.
     *
     * This method validates the token and processes the acceptance of the invitation.
     * If the invitation is valid, it updates the state of the invitation, assigns
     * the user to the project, and links the user to the appropriate role.
     *
     * @param token The token associated with the invitation.
     * @return A URL to redirect the user after successfully accepting the invitation.
     */
    String acceptInvitation(String token);

    /**
     * Archives an invitation, marking it as no longer active.
     *
     * This method is used to mark an invitation as archived. Archived invitations
     * are considered inactive and cannot be used anymore.
     *
     * @param invitation The invitation to archive.
     * @return True if the invitation was successfully archived, false otherwise.
     */
    boolean archiveInvitation(Invitations invitation);

    /**
     * Checks if an invitation is expired.
     *
     * This method determines if the specified invitation has expired based on
     * its expiration date or other conditions.
     *
     * @param invitationId The ID of the invitation to check.
     * @return True if the invitation is expired, false otherwise.
     */
    boolean isInvitationExpired(Integer invitationId);

    /**
     * Finds an invitation by its invitation link.
     *
     * This method retrieves an invitation using the unique invitation link.
     * The link is typically sent to the recipient via email.
     *
     * @param invitationLink The invitation link to search for.
     * @return The invitation with the specified invitation link, or null if not found.
     */
    Invitations findInvitationByInvitationLink(String invitationLink);

    /**
     * Retrieves invitations based on user context.
     *
     * This method retrieves invitations from the system. If the {@code isForCurrentUser} parameter is {@code true},
     * it will return only the invitations associated with the specified {@code userId}. If {@code isForCurrentUser}
     * is {@code false} or not provided, it will return all invitations, regardless of the user.
     *
     * @param userId The ID of the user. Used to filter invitations when {@code isForCurrentUser} is {@code true}.
     * @param isForCurrentUser If {@code true}, filters invitations to only show those for the specified user.
     *                         If {@code false} or {@code null}, all invitations are returned.
     * @return A list of invitations based on the provided parameters.
     */
    List<Invitations> listAll(Integer userId, Boolean isForCurrentUser);


    /**
     * Updates an invitation with new details.
     *
     * This method is used to persist changes to an invitation object, such as
     * updating its state or other attributes.
     *
     * @param invitation The invitation to update.
     */
    void updateInvitation(Invitations invitation);
}
