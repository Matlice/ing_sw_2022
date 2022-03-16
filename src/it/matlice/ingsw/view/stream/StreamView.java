package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.controller.MenuAction;
import it.matlice.ingsw.view.View;
import it.matlice.ingsw.view.menu.Menu;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class StreamView implements View {

    private final PrintStream out;
    private final Scanner in;

    public StreamView(PrintStream out, Scanner in) {
        this.out = out;
        this.in = in;
    }

    @Override
    public String[] changePassword() throws Exception {
        String psw1 = this.get("Nuova password");
        String psw2 = this.get("Repeat password");
        //if (!psw1.equals(psw2))
        //    throw new Exception("Password does not match!"); todo
        return new String[]{psw1, psw2};
    }

    @Override
    public String getNewConfiguratorUsername() {
        return this.get("New Configurator username");
    }

    @Override
    public void showNewConfiguratorUserAndPassword(String username, String password) {
        this.out.println("Use " + username + ":" + password + " to login");
    }

//    @Override
//    public void message(String title, String text) {
//        this.out.println(title + ": " + text);
//    }

    @Override
    public void info(String text) {
        this.out.println(text);
    }

    @Override
    public void warn(String text) {
        this.out.println("WARNING: " + text);
    }

    @Override
    public void error(String text) {
        this.out.println("ERROR: " + text);
    }

    @Override
    public String getLoginUsername() {
        return this.get("Utente");
    }

    @Override
    public String getPassword() {
        return this.getLine("Password");
    }

    @Override
    public String get(String prompt) {
        this.out.print(prompt + "> ");
        String r = this.in.next();
        this.in.nextLine();
        return r;
    }

    @Override
    public String getLine(String prompt) {
        this.out.print(prompt + "> ");
        return this.in.nextLine();
    }

    @Override
    public <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices, String prompt, T default_return) {
        Menu menu = new Menu();
        for (var act : choices) {
            if (act.getIndex() == null) {
                menu.addEntry(act.getName(), (in, out, ref) -> act).disable(act.isDisabled());
            } else {
                menu.addEntry(act.getIndex(), act.getName(), (in, out, ref) -> act, act.getPosition()).disable(act.isDisabled());
            }
        }
        menu.setPrompt(prompt);
        Object answ = null;
        while (answ == null) {
            answ = menu.displayOnce(this.in, this.out);
        }
        //var answ = menu.displayOnce(this.in, this.out);
        //return answ == null ? null : (MenuAction<T>) answ;
        return (MenuAction<T>) answ;
    }
}
