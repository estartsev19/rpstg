package ru.estartsev.rpstg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.estartsev.rpstg.services.GameService;

@Component
public class CallbackQueryExecutor {

    @Autowired
    private GameService gameService;

    public EditMessageText callbackQueryExecute(CallbackQuery callbackQuery) {
        String callData = callbackQuery.getData();
        long messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        String result = gameService.getGameResult(callData, callbackQuery.getFrom().getId());
        EditMessageText newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(Math.toIntExact(messageId));
        newMessage.setText(result);
        return newMessage;
    }
}
