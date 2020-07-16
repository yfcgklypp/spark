package com.example.controller;

import com.example.task.HumorousJokeTask;
import com.example.task.WeatherTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MailController
 * @Author yupanpan
 * @Date 2020/6/18 13:24
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private WeatherTask weatherTask;
    @Autowired
    private HumorousJokeTask HumorousJokeTask;

    @PostMapping("/send")
    public void send() throws Exception {
//        weatherTask.sendWeather();
        HumorousJokeTask.sendJoke();
    }
}
