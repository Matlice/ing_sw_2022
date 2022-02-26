package it.matlice.ingsw.auth.password;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.AuthMethod;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;

/**
 * Rappresenta il metodo di autenticazione mediante password.
 * La classe è un wrapper di una classe PasswordAuthenticable.
 */
public class PasswordAuthMethod implements AuthMethod {

    private final PasswordAuthenticable user;

    public PasswordAuthMethod(PasswordAuthenticable user) {
        this.user = user;
    }

    /**
     * Metodo ausiliario che ritorna un istanza valida di authdata per la classe.
     *
     * @param password password di login
     * @return un'istanza di AuthData valida.
     */
    public static AuthData getAuthData(String password) {
        return new PasswordAuthData(password);
    }

    /**
     * questo metodo permette di impostare la password dell'utente
     *
     * @param password nuova password che rispetti i parametri impostati nella configurazione
     * @throws InvalidPasswordException
     */
    public void setPassword(String password) throws InvalidPasswordException {
        //todo verifica password con regex? qualcosa come /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_()-=+])(?=.{8,})$/
        this.user.setPassword(password);
    }

    /**
     * la funzione effettua il procedimento di autenticazione in funzione della sua istanza.
     * È necessario che venga asserito che data sia di un tipo valido all'autenticazione con password
     *
     * @param data dati necessari all'autenticazione
     * @return true se l'autenticazione ha successo.
     */
    @Override
    public boolean performAuthentication(AuthData data) {
        assert data instanceof PasswordAuthData;
        //TODO verificare che base64encode(hmac_sha256(data.password, data=user.salt)) == user.password_hash
        // se user.last_login è null, forzare il cambio password.

        return true;
    }
}
