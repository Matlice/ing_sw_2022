package it.matlice.ingsw.model.data.storage;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.storage.repositories.MessageRepository;
import it.matlice.ingsw.model.exceptions.DBException;

import java.util.Calendar;
import java.util.List;

/**
 * Rappresenta una classe che si occupa di istanziare elementi di tipo Message
 * una volta caricati da una base di dati esterna.
 */
public interface MessageStorageManagement extends MessageRepository {

    /**
     * Permette di aggiungere un nuovo messaggio durante la fase di proposta di uno scambio
     * @param offer offerta a cui si riferisce il messaggio (del ricevitore)
     * @param location luogo proposto
     * @param date data proposta
     * @return messaggio inviato
     * @throws DBException errore di database durante la creazione del messaggio
     */
    Message send(Offer offer, String location, Calendar date) throws DBException;

    /**
     * Permette di aggiungere un nuovo messaggio in risposta ad una proposta di uno scambio
     * @param msg messaggio a cui rispondere
     * @param offer offerta a cui si riferisce il messaggio (del ricevitore)
     * @param location luogo proposto
     * @param date data proposta
     * @return messaggio inviato
     * @throws DBException errore di database durante la creazione del messaggio
     */
    Message answer(Message msg, Offer offer, String location, Calendar date) throws DBException;

}
