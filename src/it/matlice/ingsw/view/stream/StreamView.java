package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.controller.MenuAction;
import it.matlice.ingsw.view.View;
import it.matlice.ingsw.view.menu.Menu;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Classe che implementa l'interfaccia View dell'applicazione
 * Si interfaccia all'utente tramite stream di input e di output (ad esempio stdin e stdout)
 */
public class StreamView implements View {

    private final PrintStream out;
    private final Scanner in;

    /**
     * Costruttore per StreamView
     * @param out stream di output verso l'utente
     * @param in stream di input dall'utente
     */
    public StreamView(PrintStream out, Scanner in) {
        this.out = out;
        this.in = in;
    }

    /**
     * Comunica un messaggio all'utente
     *
     * @param text il testo del messaggio
     */
    @Override
    public void info(String text) {
        this.out.println(text);
    }

    /**
     * Comunica un avvertimento all'utente
     *
     * @param text il testo del messaggio
     */
    @Override
    public void warn(String text) {
        this.out.println("AVVISO: " + text);
    }

    /**
     * Comunica un errore all'utente
     *
     * @param text il testo dell'errore
     */
    @Override
    public void error(String text) {
        this.out.println("ERRORE: " + text);
    }

    /**
     * Richiede all'utente l'inserimento di una password
     * (separato dal metodo `get()` per far sì che l'inserimento della password possa essere offuscato
     *
     * @return la password inserita
     */
    @Override
    public String getPassword() {
        return this.getLine("Password");
    }

    /**
     * Richiede all'utente l'inserimento di una password
     * (separato dal metodo `get()` per far sì che l'inserimento della password possa essere offuscato
     *
     * @param prompt prompt della richiesta della password
     * @return la password inserita
     */
    @Override
    public String getPassword(String prompt) {
        return this.getLine(prompt);
    }

    /**
     * Richiede all'utente l'inserimento di una stringa
     *
     * @param prompt
     * @return la stringa inserita
     */
    @Override
    public String get(String prompt) {
        this.out.print(prompt + "> ");
        String r = this.in.next();
        this.in.nextLine();
        return r;
    }

    /**
     * Richiede all'utente l'inserimento di una stringa (compresa di blanks)
     *
     * @param prompt
     * @return la stringa inserita
     */
    @Override
    public String getLine(String prompt) {
        this.out.print(prompt + "> ");
        return this.in.nextLine();
    }

    /**
     * Richiede all'utente la scelta di un'opzione
     *
     * @param choices lista di opzioni disponibili tra cui scegliere
     * @param prompt  prompt della richiesta di scelta
     * @return l'azione scelta dall'utente
     */
    @Override
    public <T> MenuAction<T> chooseOption(@NotNull List<MenuAction<T>> choices, String prompt) {
        Menu menu = new Menu();
        for (var act : choices) {
            if (act.getIndex() == null) {
                menu.addEntry(act.getName(), (in, out, ref) -> act).disable(act.isDisabled());
            } else {
                menu.addEntry(act.getIndex(), act.getName(), (in, out, ref) -> act, act.getPosition()).disable(act.isDisabled());
            }
        }
        menu.setPrompt(prompt);
        Object answ = menu.displayOnce(this.in, this.out);
        while (answ == null) {
            this.error("Azione non permessa");
            answ = menu.displayOnce(this.in, this.out);
        }

        return (MenuAction<T>) answ;
    }

}
