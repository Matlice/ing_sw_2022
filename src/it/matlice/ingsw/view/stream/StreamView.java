package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.controller.MenuAction;
import it.matlice.ingsw.view.View;
import it.matlice.ingsw.view.menu.Menu;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;

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
     * @param separated true se si separa dal contesto del precedente messaggio
     */
    @Override
    public void info(String text, boolean separated) {
        if (separated) this.out.println();
        this.out.println(text);
    }

    /**
     * Comunica un messaggio all'utente
     *
     * @param text il testo del messaggio
     */
    @Override
    public void info(String text) {
        this.info(text, false);
    }

    /**
     * Comunica un avvertimento all'utente
     *
     * @param text il testo del messaggio
     * @param separated true se si separa dal contesto del precedente messaggio
     */
    @Override
    public void warn(String text, boolean separated) {
        if (separated) this.out.println();
        this.out.println("AVVISO: " + text);
    }

    /**
     * Comunica un avvertimento all'utente
     *
     * @param text il testo del messaggio
     */
    @Override
    public void warn(String text) {
        this.warn(text, false);
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
     * Show a list of objects as strings
     *
     * @param message message to show before the list
     * @param list    list of string to show
     */
    @Override
    public void showList(String message, List<String> list) {
        StringJoiner sj = new StringJoiner("\n\t");
        sj.add(message);
        list.forEach(sj::add);
        this.out.println();
        this.info(sj.toString());
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
     * Ritorna anche stringa vuota
     *
     * @param prompt messaggio di richiesta all'utente
     * @return la stringa inserita
     */
    @Override
    public String getLine(String prompt) {
        this.out.print(prompt + "> ");
        return this.in.nextLine();
    }

    /**
     * Richiede all'utente l'inserimento di una stringa (a cui sono rimossi i blank iniziali e finali)
     * Permette di specificare se la stringa immessa deve essere non vuota
     *
     * @param prompt messaggio di richiesta all'utente
     * @param canBeEmpty false if the input string must not be empty
     * @return la stringa inserita
     */
    @Override
    public String getTrimmedLine(String prompt, boolean canBeEmpty) {
        String r = this.getLine(prompt).trim();

        while (!canBeEmpty && r.length() == 0) {
            this.error("La stringa inserita non deve essere vuota");
            r = this.getLine(prompt).trim();
        }

        return r;
    }

    /**
     * Richiede all'utente l'inserimento di un valore intero
     *
     * @param prompt messaggio di richiesta all'utente
     * @param available returns true se l'intero inserito è valido, false per richiederlo
     * @param nonValidErrorMessage messaggio di errore per valori non validi
     * @return l'intero inserito
     */
    @Override
    public int getInt(String prompt, Function<Integer, Boolean> available, String nonValidErrorMessage) {
        this.out.println();
        while (true) {
            try {
                String input = this.getLine(prompt);
                int v = Integer.parseInt(input);

                if (available.apply(v))
                    return v;

                this.error(nonValidErrorMessage);

            } catch (NumberFormatException e) {
                this.error(nonValidErrorMessage);
            }
        }
    }

    /**
     * Richiede all'utente l'inserimento di un valore intero
     *
     * @param prompt    messaggio di richiesta all'utente
     * @param available returns true se l'intero inserito è valido, false per richiederlo
     * @return l'intero inserito
     */
    @Override
    public int getInt(String prompt, Function<Integer, Boolean> available) {
        return this.getInt(prompt, available, "Valore inserito non valido");
    }

    /**
     * Richiede all'utente l'inserimento di un valore intero
     *
     * @param prompt messaggio di richiesta all'utente
     * @return l'intero inserito
     */
    @Override
    public int getInt(String prompt) {
        return this.getInt(prompt, (e) -> true);
    }

    /**
     * Ritorna una lista di oggetti, inseriti dall'utente come stringa dall'utente
     * e convertiti tramite una mappa di conversione
     *
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @param conversionMap funzione che mappa i possibili input (stringhe) agli oggetti V
     *                      deve ritornare null per valori di stringhe non validi
     * @param duplicateErrorMessage messaggio di errore per valori già inseriti
     * @param nonValidErrorMessage messaggio di errore per valori non validi
     * @return lista di oggetti inseriti dall'utente
     */
    public <V> List<V> getGenericList(String prompt, boolean unique, Function<String, V> conversionMap, String duplicateErrorMessage, String nonValidErrorMessage) {
        this.out.println();
        String lastInput = "";
        List<V> list = new ArrayList<>();
        while (list.size() < 1 || lastInput.length() != 0) {
            lastInput = this.getLine(prompt + " (oppure nulla per terminare l'inserimento)").trim();
            if (lastInput.length() >= 1) {
                V toAdd = conversionMap.apply(lastInput);
                if (!unique || !list.contains(toAdd)) {
                    if (toAdd != null) {
                        list.add(toAdd);
                    } else {
                        this.error(nonValidErrorMessage);
                    }
                } else {
                    this.error(duplicateErrorMessage);
                }
            }
        }
        return list;
    }

    /**
     * Ritorna una lista di oggetti, inseriti dall'utente come stringa dall'utente
     * e convertiti tramite una mappa di conversione
     *
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @param conversionMap funzione che mappa i possibili input (stringhe) agli oggetti V
     *                      deve ritornare null per valori di stringhe non validi
     * @return lista di oggetti inseriti dall'utente
     */
    public <V> List<V> getGenericList(String prompt, boolean unique, Function<String, V> conversionMap) {
        return this.getGenericList(prompt, unique, conversionMap, "Valore già inserito", "Valore non valido");
    }

    /**
     * Ritorna una lista di stringhe non vuote inserite dall'utente
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @param duplicateErrorMessage messaggio di errore per valori già inseriti
     * @param nonValidErrorMessage messaggio di errore per valori non validi
     * @return lista di stringhe inserite dall'utente
     */
    public List<String> getStringList(String prompt, boolean unique, String duplicateErrorMessage, String nonValidErrorMessage) {
        return this.getGenericList(prompt, unique, (e) -> e, duplicateErrorMessage, nonValidErrorMessage);
    }

    /**
     * Ritorna una lista di stringhe non vuote inserite dall'utente
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @return lista di stringhe inserite dall'utente
     */
    public List<String> getStringList(String prompt, boolean unique) {
        return this.getGenericList(prompt, unique, (e) -> e);
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
