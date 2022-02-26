package it.matlice.ingsw.data;

/**
 * Interfaccia che rappresenta una classe in grado di istanziare User nella giusta declinazione
 * a partire da una base di dati
 */
public interface UserFactory {

    /**
     * @param username username dell'utente voluto (univoco)
     * @return l'utente tratto dalla base di dati
     * @throws Exception
     */
    User getUser(String username) throws Exception;

    /**
     * crea un utente e lo salva nella bs
     *
     * @param username username del nuovo utente
     * @param userType tipo di utente
     * @return l'utente creato
     * @throws Exception
     */
    User createUser(String username, User.UserTypes userType) throws Exception;

}
