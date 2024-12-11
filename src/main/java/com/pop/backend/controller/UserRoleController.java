package com.pop.backend.controller;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.service.IUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-16
 */
@Controller
@RequestMapping("/userRole")
public class UserRoleController {

    @Autowired
    IUserRoleService userRoleService;


    @PostMapping("/removeStudentsFromProject")
    @Operation(
            summary = "Allows a supervisor to remove multiple students from their project team. The students will become spectators.",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<ApiResponse<String>> removeTeamMembers(
            @RequestParam("projectId") Integer projectId,
            @RequestBody List<Integer> userIds) {

        try {
            ApiResponse<String> response = userRoleService.removeTeamMembers(projectId, userIds);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while removing team members.", null));
        }
    }


}
