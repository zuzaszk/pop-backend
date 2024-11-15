package com.pop.backend.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    private EvaluationsMapper evaluationsMapper;



    @Override
    public List<Projects> listAll(String title, Integer year, Integer language) {
        return projectsMapper.listAll(title, year, language);
    }

    @Override
    public Projects getBasicProjectInfoById(Integer projectId) {

        // Retrieve project details with users and edition info
        Projects project = projectsMapper.getProjectWithUsersAndEditionById(projectId);

        List<Evaluations> evaluations = evaluationsMapper.getEvaluationsByProjectId(projectId);
        project.setEvaluations(evaluations);

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
        Projects newProject = projectsMapper.getBasicProjectInfoById(projects.getProjectId());
        newProject.setAcronym(projects.getAcronym());
        newProject.setDescription(projects.getDescription());
        newProject.setLanguage(projects.getLanguage());
        newProject.setKeywords(projects.getKeywords());
        newProject.setEditionId(projects.getEditionId());
        newProject.setCreatedAt(LocalDateTime.now());
        newProject.setIsArchived(false);
        newProject.setIsComplete(false);

        projectsMapper.updateById(newProject);
        return newProject.getProjectId();
    }

}