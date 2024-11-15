package com.pop.backend.service;

import com.pop.backend.entity.Evaluations;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pop.backend.entity.Projects;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-14
 */
public interface IEvaluationsService extends IService<Evaluations> {

    List<Projects> getProjectsAssignedToUser(Integer userId, Integer roleId, Integer editionId);

    void addEvaluation(Evaluations evaluation);

}
