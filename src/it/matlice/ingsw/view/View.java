package it.matlice.ingsw.view;

import it.matlice.ingsw.model.MenuAction;

import java.util.List;

public interface View {

    String changePassword() throws Exception;

    void message(String title, String text);

    String getLoginUsername();

    String getPassword();

    String get(String prompt);

    <T> MenuAction<T> choose(List<MenuAction<T>> choices, String prompt, T default_return);

    default <T> MenuAction<T> choose(List<MenuAction<T>> choices) {
        return this.choose(choices, "", null);
    }

    default <T> MenuAction<T> choose(List<MenuAction<T>> choices, T default_return) {
        return this.choose(choices, "", default_return);
    }
}
