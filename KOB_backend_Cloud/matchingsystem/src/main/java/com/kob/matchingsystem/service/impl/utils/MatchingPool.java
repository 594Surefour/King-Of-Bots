package com.kob.matchingsystem.service.impl.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class MatchingPool extends Thread{
    private static List<Player> players = new LinkedList<>();
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userid, Integer rating, Integer botId){
        lock.lock();
        try{
            players.add(new Player(userid, rating, botId, 0));
        }finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userid){
        lock.lock();
        try{
            List<Player> newplayers = new ArrayList<>();
            for(Player player : players){
                if(player.getUId().equals(userid))
                    continue;
                newplayers.add(player);
            }
            players = newplayers;
        }finally {
            lock.unlock();
        }
    }

    public void increaseWaitingTime(){
        for(Player player : players){
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private boolean checkMatched(Player a, Player b){
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    public void sendResult(Player a, Player b){
        log.info("result " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_id", b.getUId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    public void matchPlayers(){
        log.info("start game " + players.toString());
        boolean[] used = new boolean[players.size()];
        for(int i = 0; i < players.size(); i++){
            if(used[i]) continue;
            for(int j = i + 1; j < players.size(); j++){
                if(used[j]) continue;
                Player a = players.get(i);
                Player b = players.get(j);
                if(checkMatched(a, b)){
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }
        List<Player> newPlayer = new LinkedList<>();
        for (int i = 0; i < players.size(); i++) {
            if(used[i])
                continue;
            newPlayer.add(players.get(i));
        }
        players = newPlayer;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try{
                    increaseWaitingTime();
                    matchPlayers();
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
