package ru.estartsev.rpstg.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class HelpCommand extends BotCommand {
    private static final Logger log = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand() {
        super("help", "Помощь");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat.getId());
        String answer = getHelp(chat);
        sendMessage.setText(answer);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private String getHelp(Chat chat) {
        if (!chat.getType().equals("private")) {
            return "Запросы можно отправлять только из приватных чатов";
        }
        return """
                Список команд:
                /play Начать новую игру
                /stats Статистика
                /help Помощь
                """;
    }
}
