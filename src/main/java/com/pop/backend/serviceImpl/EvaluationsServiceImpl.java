package com.pop.backend.serviceImpl;

import com.pop.backend.entity.Evaluations;
import com.pop.backend.entity.Projects;
import com.pop.backend.mapper.EvaluationsMapper;
import com.pop.backend.mapper.ProjectsMapper;
import com.pop.backend.service.IEvaluationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-14
 */
@Service
public class EvaluationsServiceImpl extends ServiceImpl<EvaluationsMapper, Evaluations> implements IEvaluationsService {

    @Autowired
    private ProjectsMapper projectsMapper;

    @Autowired
    private EvaluationsMapper evaluationsMapper;

    @Override
    public List<Projects> getProjectsAssignedToUser(Integer userId, Integer roleId, Integer editionId) {
        return projectsMapper.getProjectsAssignedToUser(userId, roleId, editionId);
    }

    @Override
    public void addEvaluation(Evaluations evaluation) {
        evaluation.setCreatedAt(LocalDateTime.now());
        evaluationsMapper.insert(evaluation);

    }

}
