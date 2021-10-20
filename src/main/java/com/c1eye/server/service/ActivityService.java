package com.c1eye.server.service;

import com.c1eye.server.model.Activity;
import com.c1eye.server.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public Activity getByName(String name) {
        return activityRepository.findByName(name);
    }

}
