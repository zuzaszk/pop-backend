package com.pop.backend.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pop.backend.config.EvaluationWeightsConfig;
import com.pop.backend.entity.ProjectElements;
import com.pop.backend.entity.Reviews;
import com.pop.backend.enums.EvaluationRole;
import com.pop.backend.mapper.ProjectElementsMapper;
import com.pop.backend.mapper.ReviewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.entity.Evaluations;
import com.pop.backend.entity.Projects;
import com.pop.backend.mapper.EvaluationsMapper;
import com.pop.backend.mapper.ProjectsMapper;
import com.pop.backend.service.IProjectsService;


@Service
public class ProjectsServiceImpl extends ServiceImpl<ProjectsMapper, Projects> implements IProjectsService {

    
    @Autowired
    private ProjectsMapper projectsMapper;

    @Autowired
    private AuditLogsServiceImpl auditLogService;


    @Autowired
    private ReviewsMapper reviewsMapper;

    @Autowired
    private ProjectElementsMapper projectElementsMapper;

    @Autowired
    private final EvaluationWeightsConfig evaluationWeightsConfig;

    @Autowired
    public ProjectsServiceImpl(EvaluationWeightsConfig evaluationWeightsConfig) {
        this.evaluationWeightsConfig = evaluationWeightsConfig;
    }


    @Value("${evaluation.weights.ASSIGNED_TO_EVALUATE}")
    private Double assignedToEvaluateWeight;

    @Value("${evaluation.weights.SUPERVISOR}")
    private Double supervisorWeight;

    @Value("${evaluation.weights.GENERAL_TEACHING_MEMBER}")
    private Double generalTeachingMemberWeight;

    @Value("${evaluation.weights.STUDENT}")
    private Double studentWeight;

    @Value("${evaluation.weights.SPECTATOR}")
    private Double spectatorWeight;






    @Override
    public List<Projects> listAll(String title, Integer year, Integer language) {
        return projectsMapper.listAll(title, year, language);
    }

    @Override
    public Projects getBasicProjectInfoById(Integer projectId) {

        // Retrieve project details with users and edition info
        Projects project = projectsMapper.getProjectWithUsersAndEditionById(projectId);

        List<Reviews> reviews = reviewsMapper.getReviewsByProjectId(projectId);
        project.setReviews(reviews);

        List<ProjectElements> projectElements = projectElementsMapper.getElementsByProjectId(projectId);
        project.setElements(projectElements);

        return project;
    }

    @Override
    public void createProject(String title, Integer editionId) {
        Projects project = new Projects();
        project.setTitle(title);
        project.setEditionId(editionId);
        project.setCreatedAt(LocalDateTime.now());
        project.setIsArchived(false);
        project.setIsComplete(false);
        projectsMapper.insert(project);

        auditLogService.logAction(
                "Projects",
                project.getProjectId(),
                "CREATE",
                //TODO add user
                null,
                "Created new project with title: " + title
        );

    }


    @Override
    public Integer saveBasicInfo(Projects projects) {
        Projects newProject = projectsMapper.selectById(projects.getProjectId());
        newProject.setAcronym(projects.getAcronym());
        newProject.setDescription(projects.getDescription());
        newProject.setLanguage(projects.getLanguage());
        newProject.setKeywords(projects.getKeywords());
        newProject.setEditionId(projects.getEditionId());
        newProject.setOverview(projects.getOverview());
        newProject.setCreatedAt(LocalDateTime.now());
        newProject.setIsArchived(false);
        newProject.setIsComplete(false);

        projectsMapper.updateById(newProject);
        return newProject.getProjectId();
    }



    @Override
    public List<Map<String, Object>> getTopTechnologies(Integer editionId) {
        return projectsMapper.getTopTechnologies(editionId);
    }




    @Override
    public List<Map<String, Object>> listProjectEvaluationDetails(Integer editionId) {
        List<Map<String, Object>> stats = projectsMapper.listProjectEvaluationDetails(editionId);

        // Convert weights from configuration
        Map<Integer, Double> weights = new HashMap<>();
        weights.put(EvaluationRole.ASSIGNED_TO_EVALUATE.getId(), assignedToEvaluateWeight);
        weights.put(EvaluationRole.SUPERVISOR.getId(), supervisorWeight);
        weights.put(EvaluationRole.GENERAL_TEACHING_MEMBER.getId(), generalTeachingMemberWeight);
        weights.put(EvaluationRole.STUDENT.getId(), studentWeight);
        weights.put(EvaluationRole.SPECTATOR.getId(), spectatorWeight);


        // Organize data by project
        Map<Integer, Map<String, Object>> projectData = new HashMap<>();
        for (Map<String, Object> record : stats) {
            Integer projectId = (Integer) record.get("projectid");
            String projectName = (String) record.get("projectname");
            Integer evaluationRoleId = (Integer) record.get("evaluationroleid");
            Long evaluationCount = (Long) record.get("evaluationcount");
            Double averageScore = record.get("averagescore") != null ? ((Number) record.get("averagescore")).doubleValue() : 0.0;

            // Prepare project data
            projectData.putIfAbsent(projectId, new HashMap<>());
            Map<String, Object> project = projectData.get(projectId);
            project.put("projectId", projectId);
            project.put("projectName", projectName);

            // Collect evaluation data
            List<Map<String, Object>> evaluations = (List<Map<String, Object>>) project.computeIfAbsent("evaluations", k -> new ArrayList<>());
            if (evaluationRoleId != null) {
                evaluations.add(Map.of(
                        "evaluationRole", EvaluationRole.getRoleNameById(evaluationRoleId),
                        "evaluationCount", evaluationCount,
                        "averageScore", averageScore
                ));

                // Calculate the final weighted score
                Double weight = weights.getOrDefault(evaluationRoleId, 0.0);
                project.put("finalWeightedScore", ((Double) project.getOrDefault("finalWeightedScore", 0.0)) + averageScore * weight);
            } else {
                // Log or handle the case where evaluationRoleId is null, if necessary
                System.out.println("Skipping record with null evaluationRoleId for projectId: " + projectId);
            }

        }

        return new ArrayList<>(projectData.values());
    }

    @Override
    public List<Projects> getProjectsByUserRole(Integer userId, Integer roleId, String title, Integer year, Integer language) {
        return projectsMapper.getProjectsByUserRole(userId, roleId, title, year, language);
    }


}