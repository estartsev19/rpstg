package ru.estartsev.rpstg.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.estartsev.rpstg.entity.Player;
import ru.estartsev.rpstg.repository.PlayerRepository;


@Component
public class StatsCommand extends BotCommand {
    private static final Logger log = LoggerFactory.getLogger(StatsCommand.class);

    @Autowired
    private PlayerRepository playerRepository;

    public StatsCommand() {
        super("stats", "Статистика");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(getAnswer(user, chat));
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    String getAnswer(User user, Chat chat) {
        if (!chat.getType().equals("private")) {
            return "Запросы можно отправлять только из приватных чатов";
        }
        Player player = playerRepository.findByUserId(user.getId()).orElse(null);
        if (player == null) {
            return "Данные о сыгранных играх не найдены";
        }
        return """
                Игр всего: %s
                Побед: %s
                Поражений: %s
                """
                .formatted(player.getGamesPlayed(), player.getGamesWon(), player.getGamesLose());
    }
}