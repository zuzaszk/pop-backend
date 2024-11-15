package com.pop.backend.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pop.backend.entity.Projects;

public interface IProjectsService extends IService<Projects> {

    List<Projects> listAll(String title, Integer year, Integer language);

    Projects getBasicProjectInfoById(Integer projectId);

    void createProject(String title, Integer editionId);

    Integer saveBasicInfo(Projects projects);

}
