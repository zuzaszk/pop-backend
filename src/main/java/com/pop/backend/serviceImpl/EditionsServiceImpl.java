package com.pop.backend.serviceImpl;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Editions;
import com.pop.backend.mapper.EditionsMapper;
import com.pop.backend.service.IEditionsService;
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
 * @since 2024-11-11
 */
@Service
public class EditionsServiceImpl extends ServiceImpl<EditionsMapper, Editions> implements IEditionsService {

    @Autowired
    private EditionsMapper editionsMapper;

    @Override
    public ApiResponse<String> addEdition(Editions edition) {
        // Validate required fields
        if (edition.getName() == null || edition.getYear() == null || edition.getSemester() == null) {
            return new ApiResponse<>(false, "Missing required fields: name, year, or semester.", null);
        }

        // Set creation timestamp
        edition.setCreatedAt(LocalDateTime.now());

        // Insert the edition into the database
        editionsMapper.insert(edition);

        return new ApiResponse<>(true, "Edition added successfully.", null);
    }



    @Override
    public List<Map<String, Object>> getAverageGradesForLastEditions(int n) {
        return editionsMapper.getAverageGradesForLastEditions(n);
    }

}
