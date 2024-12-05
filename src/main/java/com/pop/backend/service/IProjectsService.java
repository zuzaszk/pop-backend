package com.pop.backend.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pop.backend.entity.Projects;

public interface IProjectsService extends IService<Projects> {

    List<Projects> listAll(String title, Integer year, Integer language);

    Projects getBasicProjectInfoById(Integer projectId);

    void createProject(String title, Integer editionId);

    Integer saveBasicInfo(Projects projects);

    List<Map<String, Object>> getTopTechnologies(Integer editionId);

    List<Map<String, Object>> listProjectEvaluationDetails(Integer editionId);

    List<Projects> getProjectsByUserRole(Integer userId, Integer roleId, String title, Integer year, Integer language);



}
