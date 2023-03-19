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
public class StartCommand extends BotCommand {
    private static final Logger log = LoggerFactory.getLogger(StartCommand.class);

    @Autowired
    private PlayerRepository playerRepository;

    public StartCommand() {
        super("start", "Начать");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        createPlayer(user);
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(getAnswer(chat));
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    String getAnswer(Chat chat) {
        if (!chat.getType().equals("private")) {
            return "Запросы можно отправлять только из приватных чатов";
        }
        return """
                Бот, играющий в камень-ножницы-бумага
                
                Список команд:
                /play Начать новую игру
                /stats Статистика
                /help Помощь
                """;
    }

    private void createPlayer(User user) {
        Player player = new Player();
        player.setUserId(user.getId());
        playerRepository.save(player);
    }
}
