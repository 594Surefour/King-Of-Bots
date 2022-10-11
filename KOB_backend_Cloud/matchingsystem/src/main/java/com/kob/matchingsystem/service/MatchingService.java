package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer uid, Integer rating);
    String removePlayer(Integer uid);
}
