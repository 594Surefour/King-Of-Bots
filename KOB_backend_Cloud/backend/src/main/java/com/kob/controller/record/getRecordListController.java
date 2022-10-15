package com.kob.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.service.record.getRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class getRecordListController {
    @Autowired
    getRecordListService getRecordListService;

    @GetMapping("/record/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return getRecordListService.getList(page);
    }
}
