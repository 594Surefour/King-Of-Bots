package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer uid, Integer rating, Integer botId);
    String removePlayer(Integer uid);
}
