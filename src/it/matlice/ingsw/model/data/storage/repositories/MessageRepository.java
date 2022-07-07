package it.matlice.ingsw.model.data.storage.repositories;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.exceptions.DBException;

import java.util.List;

public interface MessageRepository {


    /**
     * Ottiene tutti i messaggi a cui l'utente deve fornire una risposta
     * @param u utente ricevitore dei messaggi
     * @return lista di messaggi per l'utente
     * @throws DBException errore di database durante l'ottenimento dei messaggi
     */
    List<Message> getUserMessages(User u) throws DBException;

}
