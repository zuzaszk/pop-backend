package com.pop.backend.controller;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Comments;
import com.pop.backend.service.ICommentsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-18
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    ICommentsService commentsService;

    @PostMapping("/add")
    @Operation(
            summary = "Supervisor and reviewer add comments to element",
            description = "Author: YL",
            tags = {"Comments"}
    )
    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_REVIEWER')")
    @CrossOrigin(origins = {"https://269593.kieg.science/api", "https://269593.kieg.science"})
    public ResponseEntity<ApiResponse<String>> addComment(@RequestBody Comments comments) {
        ApiResponse<String> response = commentsService.addComment(comments);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/getByElementId")
    @Operation(
            summary = "Get comments by elementId",
            description = "Author: YL",
            tags = {"Comments"}
    )
    @CrossOrigin(origins = {"https://269593.kieg.science/api", "https://269593.kieg.science"})
    public ResponseEntity<ApiResponse<List<Comments>>> getCommentsByElementId(
            @RequestParam Integer elementId) {
        try {
            List<Comments> comments = commentsService.getCommentsByElementId(elementId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Comments retrieved successfully.", comments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to retrieve comments.", null));
        }
    }

}
