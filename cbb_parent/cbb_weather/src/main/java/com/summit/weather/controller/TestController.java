package com.summit.weather.controller;

import com.summit.common.api.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msg")
public class TestController {
    @Autowired
    private NotificationService notificationService;



}
