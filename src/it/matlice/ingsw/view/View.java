package it.matlice.ingsw.view;

import it.matlice.ingsw.controller.MenuAction;

import java.util.List;

public interface View {

    String[] changePassword() throws Exception;

    String getNewConfiguratorUsername();

    void showNewConfiguratorUserAndPassword(String username, String password);

    void message(String title, String text);

    String getLoginUsername();

    String getPassword();

    String get(String prompt);

    <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices, String prompt, T default_return);

    default <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices) {
        return this.chooseOption(choices, "", null);
    }

    default <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices, T default_return) {
        return this.chooseOption(choices, "", default_return);
    }

}
