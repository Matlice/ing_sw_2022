package it.matlice.ingsw.view.stream;

import java.io.PrintStream;
import java.util.Scanner;

public interface MenuAction {
    Object onUserSelect(Scanner in, PrintStream out, MenuEntry ref);
}