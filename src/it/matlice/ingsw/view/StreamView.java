package it.matlice.ingsw.view;

import it.matlice.ingsw.model.Model;

import java.io.PrintStream;
import java.util.Scanner;

public class StreamView implements View{

    private PrintStream out;
    private Scanner in;

    public StreamView(PrintStream out, Scanner in) {
        this.out = out;
        this.in = in;
    }

    @Override
    public void changePassword() throws Exception {
        //todo
        out.print("new password> ");
        var psw1 = in.next();
        out.print("repeat password> ");
        var psw2 = in.next();
        if(!psw1.equals(psw2))
            throw new Exception("Password does not match!");
    }

    @Override
    public void message(String title, String text) {
        out.println(title + ": " + text);
    }

    @Override
    public String getLoginUsername() {
        out.print("Username> ");
        return in.next();
    }

    @Override
    public String getPassword() {
        out.print("Password> ");
        return in.next();
    }
}
