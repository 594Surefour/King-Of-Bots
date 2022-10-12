package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingpool = new MatchingPool();

    @Override
    public String addPlayer(Integer uid, Integer rating, Integer botId) {
        System.out.println("add player" + uid + " : " + rating);
        matchingpool.addPlayer(uid, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer uid) {
        System.out.println("remove player" + uid);
        matchingpool.removePlayer(uid);
        return "remove player success";
    }
}
