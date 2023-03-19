package ru.estartsev.rpstg;

import org.springframework.stereotype.Component;

@Component
public class NonCommand {

    public String nonCommandExecute() {
        return "Уточните свой запрос. Возможно, Вам поможет /help";
    }
}