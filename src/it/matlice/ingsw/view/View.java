package it.matlice.ingsw.view;

import it.matlice.ingsw.controller.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

import static it.matlice.ingsw.controller.PromptType.EMPTY_PROMPT;

/**
 * Interfaccia per la view dell'applicazione
 */
public interface View {

    InfoFactory getInfoFactory();

    /**
     * Comunica un avvertimento all'utente
     * @param warning tipo di warning
     * @param separated true se si separa dal contesto del precedente messaggio
     */
    void warn(WarningType warning, boolean separated);

    /**
     * Comunica un avvertimento all'utente
     * @param warning tipo di warning
     */
    void warn(WarningType warning);

    /**
     * Comunica un errore all'utente
     * @param error tipo di errore
     */
    void error(ErrorType error);


    /**
     * Show a list of objects as strings
     * @param message message to show before the list
     * @param list list of string to show
     */
    <T> void showList(InfoType message, List<T> list);

    /**
     * Richiede all'utente l'inserimento di una password
     * (separato dal metodo `get()` per far sì che l'inserimento della password possa essere offuscato
     * @return la password inserita
     */
    String getPassword();

    /**
     * Richiede all'utente l'inserimento di una password
     * (separato dal metodo `get()` per far sì che l'inserimento della password possa essere offuscato
     * @param prompt prompt della richiesta della password
     * @return la password inserita
     */
    String getPassword(PromptType prompt);

    /**
     * Richiede all'utente l'inserimento di una stringa
     * @return la stringa inserita
     */
    String get(PromptType prompt);

    /**
     * Richiede all'utente l'inserimento di una stringa (compresa di blanks)
     * @param prompt messaggio di richiesta all'utente
     * @return la stringa inserita
     */
    String getText(PromptType prompt);

    /**
     * Richiede all'utente l'inserimento di una stringa (compresa di blanks),
     * validata secondo una funzione
     * @param prompt messaggio di richiesta all'utente
     * @param available funzione che ritorna true se la stringa in input è valida
     * @return la stringa inserita
     */
    String getText(PromptType prompt, Function<String, Boolean> available);

    /**
     * Richiede all'utente l'inserimento di una stringa, che verrà convertita in un oggetto
     * tramite una funzione di conversione
     * @param prompt messaggio di richiesta all'utente
     * @param conversionMap funzione di conversione
     * @param error errore durante il parsing
     * @param <V> tipo di ritorno
     * @return oggetto creato da stringa
     */
    <V> V getLineWithConversion(PromptType prompt, Function<String, V> conversionMap, ErrorType error);

    /**
     * Richiede all'utente l'inserimento di un valore intero
     * @param prompt messaggio di richiesta all'utente
     * @param available returns true se l'intero inserito è valido, false per richiederlo
     * @param nonValidError errore per valori non validi
     * @return l'intero inserito
     */
    int getInt(PromptType prompt, Function<Integer, Boolean> available, ErrorType nonValidError);

    /**
     * Richiede all'utente l'inserimento di un valore intero
     * @param prompt messaggio di richiesta all'utente
     * @param available returns true se l'intero inserito è valido, false per richiederlo
     * @return l'intero inserito
     */
    int getInt(PromptType prompt, Function<Integer, Boolean> available);

    /**
     * Richiede all'utente l'inserimento di un valore intero
     * @param prompt messaggio di richiesta all'utente
     * @return l'intero inserito
     */
    int getInt(PromptType prompt);

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
    public <V> List<V> getGenericList(PromptType prompt, boolean unique, Function<String, V> conversionMap, ErrorType duplicateErrorMessage, ErrorType nonValidErrorMessage);

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
    public <V> List<V> getGenericList(PromptType prompt, boolean unique, Function<String, V> conversionMap);

    /**
     * Ritorna una lista di stringhe non vuote inserite dall'utente
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @param duplicateErrorMessage messaggio di errore per valori già inseriti
     * @param nonValidErrorMessage messaggio di errore per valori non validi
     * @return lista di stringhe inserite dall'utente
     */
    public List<String> getStringList(PromptType prompt, boolean unique, ErrorType duplicateErrorMessage, ErrorType nonValidErrorMessage);

    /**
     * Ritorna una lista di stringhe non vuote inserite dall'utente
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @return lista di stringhe inserite dall'utente
     */
    public List<String> getStringList(PromptType prompt, boolean unique);

    /**
     * Richiede all'utente la scelta di un'opzione
     * @param choices lista di opzioni disponibili tra cui scegliere
     * @param prompt prompt della richiesta di scelta
     * @param <T> tipo generico di ritorno delle azioni possibili
     * @return l'azione scelta dall'utente
     */
    <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices, PromptType prompt);

    /**
     * Richiede all'utente la scelta di un'opzione
     * @param choices lista di opzioni disponibili tra cui scegliere
     * @param <T> tipo generico di ritorno delle azioni possibili
     * @return l'azione scelta dall'utente
     */
    default <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices) {
        return this.chooseOption(choices, EMPTY_PROMPT);
    }

    /**
     * Permette all'utente di scegliere un'oggetto da una lista di oggetti
     * Ritorna null se l'utente vuole annullare l'operazione
     *
     * @param prompt     messaggio per l'utente
     * @param items      oggetti tra cui scegliere
     * @return oggetto selezionato
     */
    public <V> V selectItem(PromptType prompt, List<@NotNull V> items);

    public <V> V selectItem(PromptType prompt, List<@NotNull V> items, MenuType exit);
}
