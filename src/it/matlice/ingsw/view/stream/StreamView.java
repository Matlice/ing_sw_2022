package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.controller.ErrorType;
import it.matlice.ingsw.controller.MenuAction;
import it.matlice.ingsw.controller.WarningType;
import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.view.InfoFactory;
import it.matlice.ingsw.view.View;
import it.matlice.ingsw.view.menu.Menu;
import it.matlice.ingsw.view.stream.datatypes.*;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.matlice.ingsw.controller.ErrorType.*;

/**
 * Classe che implementa l'interfaccia View dell'applicazione
 * Si interfaccia all'utente tramite stream di input e di output (ad esempio stdin e stdout)
 */
public class StreamView implements View {

    private final PrintStream out;
    private final Scanner in;
    private final ConversionMap conversionMap;
    private final InfoFactory infoFactory;
    private final DataTypeConverter converter;

    /**
     * Costruttore per StreamView
     *
     * @param out       stream di output verso l'utente
     * @param in        stream di input dall'utente
     * @param converter
     */
    public StreamView(PrintStream out, Scanner in, DataTypeConverter converter) {
        this.out = out;
        this.in = in;
        this.converter = converter;
        this.conversionMap = new ConversionMap();
        this.infoFactory = new StreamInfoFactory(this);
    }

    public StreamView(PrintStream out, Scanner in) {
        var c = new DataTypeConverter();
        c.registerConverter(Category.class, o -> new StreamCategoryAdapter((Category) o));
        c.registerConverter(Hierarchy.class, o -> new StreamHierarchyAdapter((Hierarchy) o));
        c.registerConverter(Message.class, o -> new StreamMessageAdapter((Message) o));
        c.registerConverter(Offer.class, o -> new StreamOfferAdapter((Offer) o));

        this.out = out;
        this.in = in;
        this.converter = c;
        this.conversionMap = new ConversionMap();
        this.infoFactory = new StreamInfoFactory(this);
    }

    public InfoFactory getInfoFactory() {
        return this.infoFactory;
    }

    protected void println(String message) {
        this.out.println(message);
    }

    /**
     * Comunica un avvertimento all'utente
     *
     * @param warning messaggio di avviso
     * @param separated true se si separa dal contesto del precedente messaggio
     */
    @Override
    public void warn(WarningType warning, boolean separated) {
        if (separated) this.out.println();
        this.out.println("AVVISO: " + this.conversionMap.convertWarningToString(warning));
    }

    /**
     * Comunica un avvertimento all'utente
     *
     * @param warning messaggio di avviso
     */
    @Override
    public void warn(WarningType warning) {
        this.warn(warning, false);
    }

    /**
     * Comunica un errore all'utente
     *
     * @param error tipo di errore
     */
    @Override
    public void error(ErrorType error) {
        this.out.println("ERRORE: " + this.conversionMap.convertErrorToString(error));
    }

    /**
     * Show a list of objects as strings
     *
     * @param message message to show before the list
     * @param list    list of string to show
     */
    @Override
    public <T> void showList(String message, List<T> list) {
        StringJoiner sj = new StringJoiner("\n\t");
        sj.add(message);
        list.forEach((e) -> {
            sj.add(e.toString().replaceAll("\n", "\n\t"));
        });
        this.out.println();
        this.out.println(sj);
    }

    /**
     * Richiede all'utente l'inserimento di una password
     * (separato dal metodo `get()` per far sì che l'inserimento della password possa essere offuscato
     *
     * @return la password inserita
     */
    @Override
    public String getPassword() {
        return this.getPassword("Password");
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
        String password;
        do {
            password = this.getText(prompt);
        } while (password.isEmpty());
        return password;
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
    public String getText(String prompt) {
        this.out.print(prompt + "> ");
        return this.in.nextLine();
    }

    @Override
    public String getText(String prompt, Function<String, Boolean> available) {
        String in;
        while(!available.apply( in = this.getText(prompt) ))
            this.error(INVALID_VALUE);
        return in;
    }

    /**
     * Richiede all'utente l'inserimento di una stringa, che verrà convertita in un oggetto
     * tramite una funzione di conversione
     *
     * @param prompt               messaggio di richiesta all'utente
     * @param conversionMap        funzione di conversione
     * @param error                errore durante il parsing
     * @return oggetto creato da stringa
     */
    @Override
    public <V> V getLineWithConversion(String prompt, Function<String, V> conversionMap, ErrorType error) {
        String input;
        while (true) {
            input = this.getText(prompt).trim();
            V r = conversionMap.apply(input);
            if (r != null) {
                return r;
            } else {
                this.error(error);
            }
        }
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
        String r = this.getText(prompt).trim();

        while (!canBeEmpty && r.length() == 0) {
            this.error(STRING_MUST_NOT_BE_EMPTY);
            r = this.getText(prompt).trim();
        }

        return r;
    }

    /**
     * Richiede all'utente l'inserimento di un valore intero
     *
     * @param prompt messaggio di richiesta all'utente
     * @param available returns true se l'intero inserito è valido, false per richiederlo
     * @param error messaggio di errore per valori non validi
     * @return l'intero inserito
     */
    @Override
    public int getInt(String prompt, Function<Integer, Boolean> available, ErrorType error) {
        this.out.println();
        while (true) {
            try {
                String input = this.getText(prompt);
                int v = Integer.parseInt(input);

                if (available.apply(v))
                    return v;

                this.error(error);

            } catch (NumberFormatException e) {
                this.error(error);
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
        return this.getInt(prompt, available, INVALID_VALUE);
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
    public <V> List<V> getGenericList(String prompt, boolean unique, Function<String, V> conversionMap, ErrorType duplicateErrorMessage, ErrorType nonValidErrorMessage) {
        this.out.println();
        String lastInput = "";
        List<V> list = new ArrayList<>();
        while (list.size() < 1 || lastInput.length() != 0) {
            lastInput = this.getText(prompt + " (oppure nulla per terminare l'inserimento)").trim();
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
    public <V> List<V> getGenericList(String prompt, boolean unique, Function<String, V> conversionMap) { // todo move to view the conversion map
        return this.getGenericList(prompt, unique, conversionMap, DUPLICATE_VALUE, INVALID_VALUE);
    }

    /**
     * Ritorna una lista di stringhe non vuote inserite dall'utente
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @param duplicateErrorMessage messaggio di errore per valori già inseriti
     * @param nonValidErrorMessage messaggio di errore per valori non validi
     * @return lista di stringhe inserite dall'utente
     */
    public List<String> getStringList(String prompt, boolean unique, ErrorType duplicateErrorMessage, ErrorType nonValidErrorMessage) {
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
            this.error(ACTION_NOT_ALLOWED);
            answ = menu.displayOnce(this.in, this.out);
        }

        return (MenuAction<T>) answ;
    }

    @Override
    public <V> V selectItem(String prompt, String cancel, List<@NotNull V> items) {
        var actions = items
                .stream()
                .map((p) -> new MenuAction<V>(this.converter.getViewType(p).getStreamRepresentation(), () -> p))
                .collect(Collectors.toCollection(ArrayList::new));
        if(cancel != null)
            actions.add(0, new MenuAction<>(cancel, () -> null, false, 0, -1));
        return this.chooseOption(actions, prompt).getAction().run();
    }

    public DataTypeConverter getConverter() {
        return this.converter;
    }
}
