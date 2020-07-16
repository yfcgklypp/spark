package com.example.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName HumorousJokeTask
 * @Author yupanpan
 * @Date 2020/6/18 16:47
 */
@Component
public class HumorousJokeTask {

    @Scheduled(cron = "0 0 7 1/1 * ?")
    public void sendJoke(){

    }
}
