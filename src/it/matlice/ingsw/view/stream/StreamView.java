package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.MenuAction;
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
    public String changePassword() throws Exception {
        //todo
        this.out.print("new password> ");
        var psw1 = this.in.next();
        this.out.print("repeat password> ");
        var psw2 = this.in.next();
        if (!psw1.equals(psw2))
            throw new Exception("Password does not match!");
        return psw1;
    }

    @Override
    public void message(String title, String text) {
        this.out.println(title + ": " + text);
    }

    @Override
    public String getLoginUsername() {
        this.out.print("Username> ");
        return this.in.next();
    }

    @Override
    public String getPassword() {
        this.out.print("Password> ");
        return this.in.next();
    }

    @Override
    public String get(String prompt) {
        this.out.print(prompt + "> ");
        return this.in.next();
    }

    @Override
    public <T> MenuAction<T> choose(List<MenuAction<T>> choices, String prompt, T default_return) {
        Menu menu = new Menu();
        for (var act : choices) {
            menu.addEntry(act.getName(), (in, out, ref) -> act).disable(act.isDisabled());
        }
        menu.setPrompt(prompt);
        var answ = menu.displayOnce(this.in, this.out);
        return answ == null ? new MenuAction<>("", null, () -> default_return) : (MenuAction<T>) answ;
    }
}
