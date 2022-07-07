package it.matlice.ingsw.model.data.storage.factories;

import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.exceptions.DBException;
import it.matlice.ingsw.model.exceptions.InvalidUserTypeException;

public interface UserFactory {

    /**
     * crea un utente e lo salva nella bs
     *
     * @param username username del nuovo utente
     * @param userType tipo di utente
     * @return l'utente creato
     * @throws DBException
     * @throws InvalidUserTypeException
     */
    User createUser(String username, User.UserTypes userType) throws DBException, InvalidUserTypeException;

}
