package it.matlice.ingsw.view;

import it.matlice.ingsw.controller.MenuAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Interfaccia per la view dell'applicazione
 */
public interface View {

    /**
     * Comunica un messaggio all'utente
     * @param text il testo del messaggio
     * @param separated true se si separa dal contesto del precedente messaggio
     */
    void info(String text, boolean separated);

    /**
     * Comunica un messaggio all'utente
     * @param text il testo del messaggio
     */
    void info(String text);

    /**
     * Comunica un avvertimento all'utente
     * @param text il testo del messaggio
     * @param separated true se si separa dal contesto del precedente messaggio
     */
    void warn(String text, boolean separated);

    /**
     * Comunica un avvertimento all'utente
     * @param text il testo del messaggio
     */
    void warn(String text);

    /**
     * Comunica un errore all'utente
     * @param text il testo dell'errore
     */
    void error(String text);

    /**
     * Show a list of objects as strings
     * @param message message to show before the list
     * @param list list of string to show
     */
    void showList(String message, List<String> list);

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
    String getPassword(String prompt);

    /**
     * Richiede all'utente l'inserimento di una stringa
     * @return la stringa inserita
     */
    String get(String prompt);

    /**
     * Richiede all'utente l'inserimento di una stringa (compresa di blanks)
     * @return la stringa inserita
     */
    String getLine(String prompt);

    /**
     * Richiede all'utente l'inserimento di un valore intero
     * @param prompt messaggio di richiesta all'utente
     * @param available returns true se l'intero inserito è valido, false per richiederlo
     * @param nonValidErrorMessage messaggio di errore per valori non validi
     * @return l'intero inserito
     */
    int getInt(String prompt, Function<Integer, Boolean> available, String nonValidErrorMessage);

    /**
     * Richiede all'utente l'inserimento di un valore intero
     * @param prompt messaggio di richiesta all'utente
     * @param available returns true se l'intero inserito è valido, false per richiederlo
     * @return l'intero inserito
     */
    int getInt(String prompt, Function<Integer, Boolean> available);

    /**
     * Richiede all'utente l'inserimento di un valore intero
     * @param prompt messaggio di richiesta all'utente
     * @return l'intero inserito
     */
    int getInt(String prompt);

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
    public <V> List<V> getGenericList(String prompt, boolean unique, Function<String, V> conversionMap, String duplicateErrorMessage, String nonValidErrorMessage);

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
    public <V> List<V> getGenericList(String prompt, boolean unique, Function<String, V> conversionMap);

    /**
     * Ritorna una lista di stringhe non vuote inserite dall'utente
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @param duplicateErrorMessage messaggio di errore per valori già inseriti
     * @param nonValidErrorMessage messaggio di errore per valori non validi
     * @return lista di stringhe inserite dall'utente
     */
    public List<String> getStringList(String prompt, boolean unique, String duplicateErrorMessage, String nonValidErrorMessage);

    /**
     * Ritorna una lista di stringhe non vuote inserite dall'utente
     * @param prompt messaggio per l'inserimento
     * @param unique true se non ci possono essere ripetizioni
     * @return lista di stringhe inserite dall'utente
     */
    public List<String> getStringList(String prompt, boolean unique);

    /**
     * Richiede all'utente la scelta di un'opzione
     * @param choices lista di opzioni disponibili tra cui scegliere
     * @param prompt prompt della richiesta di scelta
     * @param <T> tipo generico di ritorno delle azioni possibili
     * @return l'azione scelta dall'utente
     */
    <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices, String prompt);

    /**
     * Richiede all'utente la scelta di un'opzione
     * @param choices lista di opzioni disponibili tra cui scegliere
     * @param <T> tipo generico di ritorno delle azioni possibili
     * @return l'azione scelta dall'utente
     */
    default <T> MenuAction<T> chooseOption(List<MenuAction<T>> choices) {
        return this.chooseOption(choices, "");
    }

}
