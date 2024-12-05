package com.pop.backend.service;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Editions;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-11
 */
public interface IEditionsService extends IService<Editions> {

    ApiResponse<String> addEdition(Editions edition);

    List<Map<String, Object>> getAverageGradesForLastEditions(int n);

}
