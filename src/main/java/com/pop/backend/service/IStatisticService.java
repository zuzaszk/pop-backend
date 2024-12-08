package com.pop.backend.service;

import java.util.Map;


/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-17
 */
public interface IStatisticService {


    Map<String, Object> getCounts(Integer roleId, Integer editionId);

    Map<String, Object> getReviewersStatistics(Integer reviewerId);


}
