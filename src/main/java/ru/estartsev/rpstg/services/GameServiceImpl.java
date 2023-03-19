package ru.estartsev.rpstg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.estartsev.rpstg.entity.Player;
import ru.estartsev.rpstg.enums.GameResult;
import ru.estartsev.rpstg.repository.PlayerRepository;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class GameServiceImpl implements GameService {

    @Autowired
    private PlayerRepository playerRepository;

    private static final String ROCK = "rock_gesture";
    private static final String SCISSORS = "scissors_gesture";
    private static final String PAPER = "paper_gesture";

    @Override
    public String getGameResult(String playerGesture, Long userId) {
        Player player = playerRepository.findByUserId(userId).orElse(null);
        if (player == null) {
            player = new Player();
            player.setUserId(userId);
        }
        String secondPlayerGesture = getSecondPlayerGesture();
        switch (playRound(playerGesture, secondPlayerGesture)) {
            case ERROR -> {
                return "Произошла ошибка!";
            }
            case WIN -> {
                player.setGamesPlayed(player.getGamesPlayed() + 1);
                player.setGamesWon(player.getGamesWon() + 1);
                playerRepository.save(player);
                return String.format("Соперник выбрал %s. Вы победили!", convertGestureString(secondPlayerGesture));
            }
            case DRAW -> {
                player.setGamesPlayed(player.getGamesPlayed() + 1);
                playerRepository.save(player);
                return String.format("Соперник выбрал %s. Ничья!", convertGestureString(secondPlayerGesture));
            }
            case DEFEAT -> {
                player.setGamesPlayed(player.getGamesPlayed() + 1);
                player.setGamesLose(player.getGamesLose() + 1);
                playerRepository.save(player);
                return String.format("Соперник выбрал %s. Вы проиграли!", convertGestureString(secondPlayerGesture));
            }
        }
        return null;
    }

    public GameResult playRound(String playerGesture, String secondPlayerGesture) {
        if (secondPlayerGesture == null) {
            return GameResult.ERROR;
        }
        if (playerGesture.equals(ROCK)) {
            switch (secondPlayerGesture) {
                case ROCK -> {
                    return GameResult.DRAW;
                }
                case SCISSORS -> {
                    return GameResult.WIN;
                }
                case PAPER -> {
                    return GameResult.DEFEAT;
                }
                default -> {
                    return GameResult.ERROR;
                }
            }
        }
        if (playerGesture.equals(SCISSORS)) {
            switch (secondPlayerGesture) {
                case ROCK -> {
                    return GameResult.DEFEAT;
                }
                case SCISSORS -> {
                    return GameResult.DRAW;
                }
                case PAPER -> {
                    return GameResult.WIN;
                }
                default -> {
                    return GameResult.ERROR;
                }
            }
        }
        if (playerGesture.equals(PAPER)) {
            switch (secondPlayerGesture) {
                case ROCK -> {
                    return GameResult.WIN;
                }
                case SCISSORS -> {
                    return GameResult.DEFEAT;
                }
                case PAPER -> {
                    return GameResult.DRAW;
                }
                default -> {
                    return GameResult.ERROR;
                }
            }
        }
        return null;
    }

    private String getSecondPlayerGesture() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
        return switch (randomNum) {
            case 0 -> ROCK;
            case 1 -> SCISSORS;
            case 2 -> PAPER;
            default -> null;
        };
    }

    private String convertGestureString(String gesture) {
        switch (gesture) {
            case ROCK -> {
                return "камень";
            }
            case PAPER -> {
                return "бумагу";
            }
            case SCISSORS -> {
                return "ножницы";
            }
            default -> {
                return "";
            }
        }
    }
}