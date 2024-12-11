package com.pop.backend.serviceImpl;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Evaluations;
import com.pop.backend.entity.Projects;
import com.pop.backend.entity.UserRole;
import com.pop.backend.enums.EvaluationRole;
import com.pop.backend.mapper.EvaluationsMapper;
import com.pop.backend.mapper.ProjectsMapper;
import com.pop.backend.mapper.UserRoleMapper;
import com.pop.backend.service.IEvaluationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Projects> getProjectsAssignedToUser(Integer userId, Integer editionId) {
        Integer evaluationRoleId = EvaluationRole.ASSIGNED_TO_EVALUATE.getId();
        List<Projects> assignedProject = projectsMapper.getProjectsAssignedToUser(userId, evaluationRoleId, editionId);

        for (Projects project : assignedProject) {
            List<UserRole> teamMembers = userRoleMapper.getUserRelatedToProject(project.getProjectId());
            project.setUserRole(teamMembers);
        }
        return assignedProject;
    }



    @Override
    public ApiResponse<String> addEvaluation(Evaluations evaluation) {
        //Check if the user has already graded this project
        Evaluations existingEvaluation = evaluationsMapper.hasUserAlreadyEvaluated(
                evaluation.getProjectId(), evaluation.getUserId()
        );
        if (existingEvaluation != null && existingEvaluation.getScore() != null) {
            return new ApiResponse<>(false, "You have already graded this project.", null);
        }

        // 2: Determine the user's relationship with the project
        Integer evaluationRoleId = null;

        // 2.1 Check if the user is explicitly assigned to evaluate by the chair
        Evaluations assignedEvaluation = evaluationsMapper.getEvaluationByUserProjectEvaluationRole(
                evaluation.getProjectId(), evaluation.getUserId(),EvaluationRole.ASSIGNED_TO_EVALUATE.getId()
        );
        if (assignedEvaluation != null) {
            assignedEvaluation.setScore(evaluation.getScore());
            if (assignedEvaluation.getComment() == null || assignedEvaluation.getComment().equals("")){
                if (evaluation.getComment()!=null){
                    assignedEvaluation.setComment(evaluation.getComment());
                }
            }
            assignedEvaluation.setUpdatedAt(LocalDateTime.now());
            assignedEvaluation.setState(2); // Evaluation Completed
            evaluationsMapper.updateById(assignedEvaluation);
            return new ApiResponse<>(true, "Evaluation added successfully.", null);
        }else{
            // 2.2 Check if the user is actually the supervisor for this project
            UserRole supervisorRole = userRoleMapper.getUserRoleForProject(
                    evaluation.getProjectId(), evaluation.getUserId(), 2 // Role ID for Supervisor
            );
            if (supervisorRole != null) {
                evaluationRoleId = EvaluationRole.SUPERVISOR.getId(); // Supervisor for this project
            }else {
                // 2.3 Check if the user has any supervisor or reviewer roles in other projects
                UserRole teachingMemberRole = userRoleMapper.isTeachingMember(
                        evaluation.getUserId()
                );
                if (teachingMemberRole != null) {
                    evaluationRoleId = EvaluationRole.GENERAL_TEACHING_MEMBER.getId(); // General teaching member
            }else {
                    // 2.4 Check if the user is a student
                    UserRole studentRole = userRoleMapper.getUserRoleForProject(
                            evaluation.getProjectId(), evaluation.getUserId(), 1 // Role ID for Student
                    );
                    if (studentRole != null) {
                        evaluationRoleId = EvaluationRole.STUDENT.getId(); // Student
                    }else {
                        // 2.5 Check if the user is a spectator
                        UserRole spectatorRole = userRoleMapper.getUserRoleForProject(
                                evaluation.getProjectId(), evaluation.getUserId(), 5 // Role ID for Spectator
                        );
                        if (spectatorRole != null) {
                            evaluationRoleId = EvaluationRole.SPECTATOR.getId(); // Spectator
                        } else {
                            // User has no valid relationship with the project
                            return new ApiResponse<>(false, "User has no valid relationship with this project.", null);
                        }
                    }
                }
            }

        }

        // Step 3: Insert the evaluation into the database
        evaluation.setEvaluationRoleId(evaluationRoleId);
        evaluation.setCreatedAt(LocalDateTime.now());
        evaluation.setUpdatedAt(LocalDateTime.now());
        evaluationsMapper.insert(evaluation);

        return new ApiResponse<>(true, "Evaluation added successfully.", null);

    }


    @Override
    public Evaluations getEvaluationByUser(Integer projectId, Integer userId) {
        return evaluationsMapper.getEvaluationByUser(projectId, userId);
    }


    @Override
    public ApiResponse<String> updateEvaluation(Evaluations evaluation) {
        // Validate that the evaluation exists
        Evaluations existingEvaluation = evaluationsMapper.selectById(evaluation.getEvaluationId());
        if (existingEvaluation == null) {
            throw new IllegalArgumentException("Evaluation with the given ID does not exist.");
        }


        // Update the fields
        existingEvaluation.setScore(evaluation.getScore());
        existingEvaluation.setComment(evaluation.getComment());
        existingEvaluation.setUpdatedAt(LocalDateTime.now());

        // Save the updated evaluation
        evaluationsMapper.updateById(existingEvaluation);

        return new ApiResponse<>(true, "Evaluation updated successfully.", null);
    }



    @Override
    public int getEvaluatedProjectsCount(int reviewerId) {
        Integer evaluatedProjectsCount = evaluationsMapper.countEvaluatedProjectsByUser(reviewerId);
        if (evaluatedProjectsCount == null) {
            return 0;
        }
        // return evaluationsMapper.countEvaluatedProjectsByUser(reviewerId);
        return evaluatedProjectsCount;
    }



    @Override
    public int getNotEvaluatedProjectsCount(int reviewerId) {
        Integer notEvaluatedProjectsCount = evaluationsMapper.countNotEvaluatedProjectsByUser(reviewerId);
        if (notEvaluatedProjectsCount == null) {
            return 0;
        }
        return notEvaluatedProjectsCount;
        // return evaluationsMapper.countNotEvaluatedProjectsByUser(reviewerId);
    }



    @Override
    public double getAverageScore(int reviewerId) {
        Double averageScore = evaluationsMapper.averageScoreByUser(reviewerId);
        if (averageScore == null) {
            return 0.0;
        }
        // return evaluationsMapper.averageScoreByUser(reviewerId);
        return averageScore;
    }



    @Override
    public List<Map<String, Integer>> getScoreDistribution(int reviewerId) {
        return evaluationsMapper.scoreDistributionByUser(reviewerId);
    }



    @Override
    public double getAverageEvaluationTime(int reviewerId) {
        Double averageHours = evaluationsMapper.averageEvaluationTimeByUser(reviewerId);
        if (averageHours == null) {
            return 0.0;
        }
        return averageHours;
    }

}
