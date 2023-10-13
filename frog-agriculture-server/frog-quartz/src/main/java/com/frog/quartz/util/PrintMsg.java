package com.frog.quartz.util;

import org.springframework.stereotype.Component;

@Component
public class PrintMsg {
    public void print(){
        System.out.println("定时任务触发");
    }
}
