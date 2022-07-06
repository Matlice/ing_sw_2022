package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.controller.*;
import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.view.InfoFactory;
import it.matlice.ingsw.view.View;
import it.matlice.ingsw.view.menu.Menu;
import it.matlice.ingsw.view.menu.MenuEntryWrapper;
import it.matlice.ingsw.view.stream.datatypes.*;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.matlice.ingsw.controller.ErrorType.*;
import static it.matlice.ingsw.controller.PromptType.*;
import static it.matlice.ingsw.controller.MenuType.*;

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
        c.registerConverter(String.class, o -> new StreamStringAdapter((String) o));
        c.registerConverter(LinkedList.class, o -> {
            assert o instanceof LinkedList<?>;
            LinkedList<?> l = (LinkedList<?>) o;
            assert !l.isEmpty() && l.get(0) instanceof Category;
            return new StreamCategoryChainAdapter((LinkedList<Category>) l);
        });
        c.registerConverter(Map.Entry.class, o -> {
            assert o instanceof Map.Entry<?, ?> entry;
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            assert (entry.getKey() instanceof String && entry.getValue() instanceof TypeDefinition);
            return new StreamFieldAdapter((Map.Entry<String, TypeDefinition>) entry);
        });

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
     * @param message to show before the list
     * @param list    list of string to show
     */
    @Override
    public <T> void showList(InfoType message, List<T> list) {
        StringJoiner sj = new StringJoiner("\n\t");
        sj.add(this.conversionMap.convertInfoToString(message));
        list.forEach((e) -> {
            sj.add(this.converter.getViewType(e).getStreamRepresentation().replaceAll("\n", "\n\t"));
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
        return this.getPassword(INSERT_PASSWORD);
    }

    /**
     * Richiede all'utente l'inserimento di una password
     * (separato dal metodo `get()` per far sì che l'inserimento della password possa essere offuscato
     *
     * @param prompt prompt della richiesta della password
     * @return la password inserita
     */
    @Override
    public String getPassword(PromptType prompt) {
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
    public String get(PromptType prompt) {
        this.out.print(this.conversionMap.convertPromptToString(prompt) + "> ");
        String r = this.in.next();
        this.in.nextLine();
        return r;
    }


    private String getText(String prompt) {
        this.out.print(prompt + "> ");
        return this.in.nextLine();
    }

    /**
     * Richiede all'utente l'inserimento di una stringa (compresa di blanks)
     * Ritorna anche stringa vuota
     *
     * @param prompt messaggio di richiesta all'utente
     * @return la stringa inserita
     */
    @Override
    public String getText(PromptType prompt) {
        this.out.print(this.conversionMap.convertPromptToString(prompt) + "> ");
        return this.in.nextLine();
    }

    @Override
    public String getText(PromptType prompt, Function<String, Boolean> available) {
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
    public <V> V getLineWithConversion(PromptType prompt, Function<String, V> conversionMap, ErrorType error) {
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
     * Richiede all'utente l'inserimento di un valore intero
     *
     * @param prompt messaggio di richiesta all'utente
     * @param available returns true se l'intero inserito è valido, false per richiederlo
     * @param error messaggio di errore per valori non validi
     * @return l'intero inserito
     */
    @Override
    public int getInt(PromptType prompt, Function<Integer, Boolean> available, ErrorType error) {
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
    public int getInt(PromptType prompt, Function<Integer, Boolean> available) {
        return this.getInt(prompt, available, INVALID_VALUE);
    }

    /**
     * Richiede all'utente l'inserimento di un valore intero
     *
     * @param prompt messaggio di richiesta all'utente
     * @return l'intero inserito
     */
    @Override
    public int getInt(PromptType prompt) {
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
    public <V> List<V> getGenericList(PromptType prompt, boolean unique, Function<String, V> conversionMap, ErrorType duplicateErrorMessage, ErrorType nonValidErrorMessage) {
        this.out.println();
        String lastInput = "";
        List<V> list = new ArrayList<>();
        while (list.size() < 1 || lastInput.length() != 0) {
            lastInput = this.getText(this.conversionMap.convertPromptToString(prompt) + " (oppure nulla per terminare l'inserimento)").trim();
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
    public <V> List<V> getGenericList(PromptType prompt, boolean unique, Function<String, V> conversionMap) {
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
    public List<String> getStringList(PromptType prompt, boolean unique, ErrorType duplicateErrorMessage, ErrorType nonValidErrorMessage) {
        return this.getGenericList(prompt, unique, (e) -> e, duplicateErrorMessage, nonValidErrorMessage);
    }

    /**
     * Ritorna una lista di stringhe non vuote inserite dall'utente
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @return lista di stringhe inserite dall'utente
     */
    public List<String> getStringList(PromptType prompt, boolean unique) {
        return this.getGenericList(prompt, unique, (e) -> e);
    }


    /**
     * Richiede all'utente la scelta di un'opzione
     *
     * @param choices lista di opzioni disponibili tra cui scegliere
     * @param prompt  prompt della richiesta di scelta
     * @return l'azione scelta dall'utente
     */
    private <T> T choose(@NotNull List<MenuEntryWrapper<T>> choices, PromptType prompt) {
        Menu menu = new Menu();
        for (var act : choices)
            menu.addEntry(act);
        menu.setPrompt(this.conversionMap.convertPromptToString(prompt));
        Object answ = menu.displayOnce(this.in, this.out);
        while (answ == null) {
            this.error(ACTION_NOT_ALLOWED);
            answ = menu.displayOnce(this.in, this.out);
        }
        return (T) answ;
    }

    /**
     * Richiede all'utente la scelta di un'opzione
     *
     * @param choices lista di opzioni disponibili tra cui scegliere
     * @param prompt  prompt della richiesta di scelta
     * @return l'azione scelta dall'utente
     */
    @Override
    public <T> MenuAction<T> chooseOption(@NotNull List<MenuAction<T>> choices, PromptType prompt) {
        return this.choose(choices.stream()
                .map((e) -> new MenuEntryWrapper<MenuAction<T>>(
                        this.conversionMap.convertMenuToString(e.getType()),
                        (in, out, ref) -> e, e.isDisabled(),
                        e.getIndex(),
                        e.getPosition()
                )).toList(), prompt);
    }

    @Override
    public <V> V selectItem(PromptType prompt, List<@NotNull V> items) {
        return this.selectItem(prompt, items, EXIT_ENTRY);
    }

    @Override
    public <V> V selectItem(PromptType prompt, List<@NotNull V> items, MenuType exit) {
        var actions = items
                .stream()
                .map(
                        (p) -> new MenuEntryWrapper<ReturnAction<V>>(
                                this.converter.getViewType(p).getStreamRepresentation(),
                                (in, out, ref) -> (ReturnAction<V>) () -> p))
                .collect(Collectors.toCollection(ArrayList::new));
        if(exit != null) actions.add(0, new MenuEntryWrapper<>(this.conversionMap.convertMenuToString(exit), (in, out, ref) -> (ReturnAction<V>) () -> null, false, 0, -1));

        return this.choose(actions, prompt).run();
    }

    public DataTypeConverter getConverter() {
        return this.converter;
    }
}
