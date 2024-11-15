package com.pop.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pop.backend.entity.Projects;

@Repository
public interface ProjectsMapper extends BaseMapper<Projects> {

    List<Projects> listAll(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("language") Integer language
    );

    Projects getBasicProjectInfoById(@Param("projectId") Integer projectId);

    Projects getProjectWithUsersAndEditionById(Integer projectId);


    List<Projects> getProjectsAssignedToUser(
            @Param("userId") Integer userId,
            @Param("roleId") Integer roleId,
            @Param("editionId") Integer editionId);


}
