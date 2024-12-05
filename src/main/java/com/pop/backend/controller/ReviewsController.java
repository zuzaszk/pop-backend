package com.pop.backend.controller;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Reviews;
import com.pop.backend.service.IReviewsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-17
 */
@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    @Autowired
    private IReviewsService reviewsService;

    @PostMapping("/add")
    @Operation(
            summary = "Add a review",
            description = "Author: YL")
    public ResponseEntity<ApiResponse<String>> addReview(@RequestBody Reviews review) {
        ApiResponse<String> response = reviewsService.addReview(review);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

}
