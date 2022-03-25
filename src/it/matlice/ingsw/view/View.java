package it.matlice.ingsw.view;

import it.matlice.ingsw.controller.MenuAction;

import java.util.List;

/**
 * Interfaccia per la view dell'applicazione
 */
public interface View {

    /**
     * Comunica un messaggio all'utente
     * @param text il testo del messaggio
     */
    void info(String text);

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
