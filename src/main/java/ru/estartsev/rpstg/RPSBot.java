package ru.estartsev.rpstg;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.estartsev.rpstg.command.HelpCommand;
import ru.estartsev.rpstg.command.PlayCommand;
import ru.estartsev.rpstg.command.StartCommand;
import ru.estartsev.rpstg.command.StatsCommand;
import ru.estartsev.rpstg.services.GameService;

@Component
public class RPSBot extends TelegramLongPollingCommandBot {
    private static final Logger log = LoggerFactory.getLogger(RPSBot.class);
    @Value("${rpsbot.name}")
    private String botName;
    @Value("${rpsbot.token}")
    private String botToken;
    @Autowired
    private NonCommand nonCommand;
    @Autowired
    private CallbackQueryExecutor callbackQueryExecutor;
    @Autowired
    private PlayCommand playCommand;
    @Autowired
    private StatsCommand statsCommand;
    @Autowired
    private HelpCommand helpCommand;
    @Autowired
    private StartCommand startCommand;

    @Autowired
    private GameService gameService;

    @PostConstruct
    public void init() {
        register(startCommand);
        register(playCommand);
        register(statsCommand);
        register(helpCommand);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String answer = nonCommand.nonCommandExecute();
            setSendMessage(message.getChatId(), getUserName(message), answer);
        } else if (update.hasCallbackQuery()) {
            EditMessageText newMessage = callbackQueryExecutor.callbackQueryExecute(update.getCallbackQuery());
            try {
                execute(newMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSendMessage(Long chatId, String userName, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.info(String.format("Пользователь %s указал некорректную строку %s", userName, text));
        }
    }

    private String getUserName(Message msg) {
        User user = msg.getFrom();
        return user.getUserName();
    }
}
