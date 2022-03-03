package it.matlice.ingsw.view;

import it.matlice.ingsw.model.MenuAction;

import java.util.List;

public interface View {

    String changePassword() throws Exception;

    void message(String title, String text);

    String getLoginUsername();

    String getPassword();

    MenuAction choose(List<MenuAction> choices);
}
