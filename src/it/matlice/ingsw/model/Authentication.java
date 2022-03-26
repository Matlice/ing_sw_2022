package it.matlice.ingsw.model;

import it.matlice.ingsw.model.data.User;

/**
 * Rappresenta un token di accesso. funzionalità simili a jwt
 */
public interface Authentication {
    /**
     * @return Ritorna l'utente al quale il login è associato
     */
    User getUser();

    /**
     * Ottiene la data di login
     *
     * @return unix time stamp del login
     */
    long getLoginTime();

    /**
     * Ottiene la data entro cui il login è valido
     *
     * @return unix time stamp della data di scadenza della sessione
     */
    long getExpirationTime();

    /**
     * @return true se la sessione è valida
     */
    boolean isValid();

    /**
     * @return una stringa che identifica l'errore di sessione o null se la sessione è valida
     */
    String loginProblem();
}
