package com.kob.service.ranklist;

import com.alibaba.fastjson.JSONObject;

public interface GetRankListService {
    JSONObject getList(Integer page);
}
