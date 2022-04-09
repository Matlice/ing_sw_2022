package it.matlice.ingsw.model.data.factories;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.User;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Rappresenta una classe che si occupa di istanziare elementi di tipo Message
 * una volta caricati da una base di dati esterna.
 */
public interface MessageFactory {

    /**
     * Permette di aggiungere un nuovo messaggio durante la fase di proposta di uno scambio
     * @param offer offerta a cui si riferisce il messaggio (del ricevitore)
     * @param location luogo proposto
     * @param date data proposta
     * @param timestamp momento di creazione del messaggio
     * @return messaggio inviato
     * @throws SQLException errore di database durante la creazione del messaggio
     */
    Message send(Offer offer, String location, Calendar date, long timestamp) throws SQLException;

    /**
     * Permette di aggiungere un nuovo messaggio in risposta ad una proposta di uno scambio
     * @param msg messaggio a cui rispondere
     * @param offer offerta a cui si riferisce il messaggio (del ricevitore)
     * @param location luogo proposto
     * @param date data proposta
     * @return messaggio inviato
     * @throws SQLException errore di database durante la creazione del messaggio
     */
    Message answer(Message msg, Offer offer, String location, Calendar date) throws SQLException;

    /**
     * Ottiene tutti i messaggi a cui l'utente deve fornire una risposta
     * @param u utente ricevitore dei messaggi
     * @return lista di messaggi per l'utente
     * @throws SQLException errore di database durante l'ottenimento dei messaggi
     */
    List<Message> getUserMessages(User u) throws SQLException;
}
