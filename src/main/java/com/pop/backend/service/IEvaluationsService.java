package com.pop.backend.service;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Evaluations;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pop.backend.entity.Projects;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-14
 */
public interface IEvaluationsService extends IService<Evaluations> {

    List<Projects> getProjectsAssignedToUser(Integer userId, Integer editionId);

    ApiResponse<String> addEvaluation(Evaluations evaluation);

    Evaluations getEvaluationByUserProjectEvaluationRole(Integer projectId, Integer userId, Integer evaluationRoleId);


    ApiResponse<String> updateEvaluation(Evaluations evaluation);

    int getEvaluatedProjectsCount(int reviewerId);

    int getNotEvaluatedProjectsCount(int reviewerId);

    double getAverageScore(int reviewerId);

    List<Map<String, Integer>> getScoreDistribution(int reviewerId);

    double getAverageEvaluationTime(int reviewerId);



}
