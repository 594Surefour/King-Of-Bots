package com.kob.service.Impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.mapper.UserMapper;
import com.kob.pojo.User;
import com.kob.service.ranklist.GetRankListService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRankListServiceImpl implements GetRankListService {
    @Autowired
    private UserMapper userMapper;



    @Override
    public JSONObject getList(Integer page) {
        IPage<User> userIPage = new Page<>(page, 5);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> userList = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        for(User user : userList){
            user.setPassword("");
        }
        resp.put("users", userList);
        resp.put("users_count", userMapper.selectCount(null));
        return resp;
    }
}
