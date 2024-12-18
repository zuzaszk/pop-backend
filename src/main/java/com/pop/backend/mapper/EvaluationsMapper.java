package com.pop.backend.mapper;

import com.pop.backend.entity.Evaluations;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author yl, za
 * @since 2024-11-14
 */
public interface EvaluationsMapper extends BaseMapper<Evaluations> {

    List<Evaluations> getEvaluationsByProjectId(Integer projectId);

    Evaluations getExistingScore(Integer projectId, Integer userId, Integer roleId);

    Evaluations hasUserAlreadyEvaluated(Integer projectId, Integer userId);

    Evaluations getEvaluationByUser(@Param("projectId") Integer projectId,
                                      @Param("userId") Integer userId);

    Evaluations getEvaluationByUserProjectEvaluationRole(@Param("projectId") Integer projectId,
                                                         @Param("userId") Integer userId,
                                                         @Param("evaluationRoleId") Integer evaluationRoleId);


    Integer countEvaluatedProjectsByUser(Integer userId);

    Integer countNotEvaluatedProjectsByUser(Integer userId);

    Double averageScoreByUser(Integer userId);

    List<Map<String, Integer>> scoreDistributionByUser(Integer userId);

    Double averageEvaluationTimeByUser(Integer userId);
}
