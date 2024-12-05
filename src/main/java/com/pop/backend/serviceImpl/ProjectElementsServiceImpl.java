package com.pop.backend.serviceImpl;

import com.pop.backend.entity.ElementTypes;
import com.pop.backend.entity.ProjectElements;
import com.pop.backend.entity.Projects;
import com.pop.backend.mapper.ElementTypesMapper;
import com.pop.backend.mapper.ProjectElementsMapper;
import com.pop.backend.mapper.ProjectsMapper;
import com.pop.backend.service.IProjectElementsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pop.backend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-12
 */
@Service
public class ProjectElementsServiceImpl extends ServiceImpl<ProjectElementsMapper, ProjectElements> implements IProjectElementsService {

    @Autowired
    private ProjectElementsMapper projectElementsMapper;
    @Autowired
    private ProjectsMapper projectsMapper;
    @Autowired
    private ElementTypesMapper elementTypesMapper;


    @Override
    public void uploadElement(Integer projectId, Integer elementTypeId, MultipartFile file) {
        // Query existing element to handle old records and files
        ProjectElements existingElement = projectElementsMapper.getByProjectIdAndElementTypeId(projectId, elementTypeId);

        ProjectElements element = new ProjectElements();
        element.setProjectId(projectId);
        element.setElementTypeId(elementTypeId);
        element.setCreatedAt(LocalDateTime.now());

        Projects projects = projectsMapper.selectById(projectId);
        String projectAcr = projects.getAcronym();
        ElementTypes elementTypes = elementTypesMapper.selectById(elementTypeId);
        String elementTypesName = elementTypes.getName();


        String filePath = CommonUtil.saveFileToStorage(file, projectAcr, elementTypesName);
        element.setVFilePath(filePath);

        projectElementsMapper.insert(element);

        // Delete old records and files after successful insertion
        if (existingElement != null) {
            // Delete old database record
            projectElementsMapper.deleteById(existingElement.getElementId());
        }

    }

    @Override
    public ResponseEntity<Resource> retrieveFile(Integer projectElementId) {
        ProjectElements projectElements = projectElementsMapper.selectById(projectElementId);
        String vFilePath = projectElements.getVFilePath();

        return CommonUtil.retrieveFile(vFilePath);


    }

//    @Override
//    public Map<String, Object> checkProjectElementsStatus(Integer projectId) {
//
//
//    }


}
