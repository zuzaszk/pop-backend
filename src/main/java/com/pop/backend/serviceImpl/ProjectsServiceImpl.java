package com.pop.backend.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.entity.Projects;
import com.pop.backend.mapper.ProjectsMapper;
import com.pop.backend.service.IProjectsService;


@Service
public class ProjectsServiceImpl extends ServiceImpl<ProjectsMapper, Projects> implements IProjectsService {

    @Autowired
    private ProjectsMapper projectsMapper;

    @Override
    public List<Projects> listAll(String title, Integer year, Integer language) {
        return projectsMapper.listAll(title, year, language);
    }

    @Override
    public Projects getBasicProjectInfoById(Integer projectId) {
        return projectsMapper.getBasicProjectInfoById(projectId);
    }

}