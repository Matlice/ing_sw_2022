package it.matlice.ingsw.view.menu;

import java.io.PrintStream;
import java.util.Scanner;

public interface MenuAction {
    Object onUserSelect(Scanner in, PrintStream out, MenuEntry ref);
}