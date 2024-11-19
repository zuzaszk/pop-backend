package com.pop.backend.mapper;

import com.pop.backend.entity.Reviews;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-17
 */
public interface ReviewsMapper extends BaseMapper<Reviews> {

    List<Reviews> getReviewsByProjectId(Integer projectId);

    int countReviewsByEdition(@Param("editionId") Integer editionId);


}
