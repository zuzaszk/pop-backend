package com.pop.backend.serviceImpl;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Reviews;
import com.pop.backend.mapper.ReviewsMapper;
import com.pop.backend.service.IReviewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-17
 */
@Service
public class ReviewsServiceImpl extends ServiceImpl<ReviewsMapper, Reviews> implements IReviewsService {

    @Autowired
    private ReviewsMapper reviewMapper;

    @Override
    public ApiResponse<String> addReview(Reviews reviews) {
        if (reviews.getProjectId() == null || reviews.getUserId() == null || reviews.getRoleId() == null || reviews.getReview() == null) {
            return new ApiResponse<>(false, "Missing required fields: projectId, userId, or comment.", null);
        }

        reviews.setCreatedAt(LocalDateTime.now());
        reviews.setIsPublic(true);
        reviewMapper.insert(reviews);

        return new ApiResponse<>(true, "Review added successfully.", null);
    }

}
