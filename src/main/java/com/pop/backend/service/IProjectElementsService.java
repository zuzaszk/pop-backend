package com.pop.backend.service;

import com.pop.backend.entity.ProjectElements;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-12
 */
public interface IProjectElementsService extends IService<ProjectElements> {

    void uploadElement(Integer projectId, Integer elementTypeId, MultipartFile file);

    ResponseEntity<Resource> retrieveFile(Integer projectElementId);

    //Map<String, Object> checkProjectElementsStatus(Integer projectId);



}
