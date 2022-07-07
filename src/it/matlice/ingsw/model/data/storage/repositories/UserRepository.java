package it.matlice.ingsw.model.data.storage.repositories;

import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.exceptions.DBException;
import it.matlice.ingsw.model.exceptions.InvalidUserException;

import java.util.List;

public interface UserRepository {

    /**
     * @param username username dell'utente voluto (univoco)
     * @return l'utente tratto dalla base di dati
     * @throws DBException
     * @throws InvalidUserException
     */
    User getUser(String username) throws DBException, InvalidUserException;

    /**
     * Ritorna la lista degli utente a database
     * @return lista di utenti
     * @throws DBException errore di database
     */
    List<User> getUsers() throws DBException;

    boolean doesUserExist(String username) throws DBException;

    /**
     * Permette di salvare un utente a database
     * @param u utente da salvare
     * @return utente salvato
     * @throws DBException errore di database
     */
    User saveUser(User u) throws DBException;

}
