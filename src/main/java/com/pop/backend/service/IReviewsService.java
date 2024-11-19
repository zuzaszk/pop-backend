package com.pop.backend.service;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Reviews;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-17
 */
public interface IReviewsService extends IService<Reviews> {

    ApiResponse<String> addReview(Reviews review);

}
