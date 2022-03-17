package it.matlice.ingsw.view;

import it.matlice.ingsw.controller.MenuAction;

import java.util.List;

public interface View {

    void info(String text);

    void warn(String text);

    void error(String text);

    String getPassword();

    String getPassword(String prompt);

    String get(String prompt);

    String getLine(String prompt);

    <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices, String prompt);

    default <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices) {
        return this.chooseOption(choices, "");
    }

}
