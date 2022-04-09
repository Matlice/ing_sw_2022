package it.matlice.ingsw.model.data.factories;

import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.InvalidUserTypeException;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia che rappresenta una classe in grado di istanziare User nella giusta declinazione
 * a partire da una base di dati
 */
public interface UserFactory {

    /**
     * @param username username dell'utente voluto (univoco)
     * @return l'utente tratto dalla base di dati
     * @throws SQLException
     * @throws InvalidUserException
     */
    User getUser(String username) throws SQLException, InvalidUserException;

    boolean doesUserExist(String username) throws SQLException;

    /**
     * crea un utente e lo salva nella bs
     *
     * @param username username del nuovo utente
     * @param userType tipo di utente
     * @return l'utente creato
     * @throws SQLException
     * @throws InvalidUserTypeException
     */
    User createUser(String username, User.UserTypes userType) throws SQLException, InvalidUserTypeException;

    /**
     * Permette di salvare un utente a database
     * @param u utente da salvare
     * @return utente salvato
     * @throws SQLException errore di database
     */
    User saveUser(User u) throws SQLException;

    /**
     * Ritorna la lista degli utente a database
     * @return lista di utenti
     * @throws SQLException errore di database
     */
    List<User> getUsers() throws SQLException;
}
