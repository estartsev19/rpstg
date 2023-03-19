package ru.estartsev.rpstg.services;

import org.springframework.stereotype.Service;

@Service
public interface GameService {

    String getGameResult(String playerGesture, Long userId);
}
