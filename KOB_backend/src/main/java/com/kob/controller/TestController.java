package com.kob.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pk")
public class TestController {
    @RequestMapping("/test")
    public Map<String, String> test1(){
        Map<String, String> mapTest1 = new HashMap<>();
        mapTest1.put("name", "tiger");
        mapTest1.put("number", "1500");
        return mapTest1;
    }
}
