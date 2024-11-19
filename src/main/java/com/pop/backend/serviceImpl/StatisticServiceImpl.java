package com.pop.backend.serviceImpl;

import com.pop.backend.mapper.ProjectsMapper;
import com.pop.backend.mapper.ReviewsMapper;
import com.pop.backend.mapper.UsersMapper;
import com.pop.backend.service.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticServiceImpl implements IStatisticService {

    @Autowired
    private ProjectsMapper projectsMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ReviewsMapper reviewsMapper;


    @Override
    public Map<String, Object> getCounts(Integer roleId, Integer editionId) {
        Map<String, Object> statistics = new HashMap<>();

        int totalUsers = usersMapper.countUsersByRole(roleId);
        statistics.put("totalUsers", totalUsers);

        int totalProjects = projectsMapper.countProjectsByEdition(editionId);
        statistics.put("totalProjects", totalProjects);

        int totalReviews = reviewsMapper.countReviewsByEdition(editionId);
        statistics.put("totalReviews", totalReviews);

        return statistics;
    }

}
