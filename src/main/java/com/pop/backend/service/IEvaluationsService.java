package com.pop.backend.service;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Evaluations;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pop.backend.entity.Projects;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-14
 */
public interface IEvaluationsService extends IService<Evaluations> {

    List<Projects> getProjectsAssignedToUser(Integer userId, Integer evaluationRoleId, Integer editionId);

    ApiResponse<String> addEvaluation(Evaluations evaluation);

    Evaluations getEvaluationByUserProjectEvaluationRole(Integer projectId, Integer userId, Integer evaluationRoleId);


    ApiResponse<String> updateEvaluation(Evaluations evaluation);



}
