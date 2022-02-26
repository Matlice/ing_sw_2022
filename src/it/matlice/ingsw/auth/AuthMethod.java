package it.matlice.ingsw.auth;

/**
 * Raoppresenta un metodo di autenticazione e compie le azioni affini
 * alle procedure di autenticazione, quali il cambio password, gestione delle scadenze ecc...
 */
public interface AuthMethod {
    /**
     * la funzione effettua il procedimento di autenticazione in funzione della sua istanza
     *
     * @param data dati necessari all'autenticazione
     * @return true se l'autenticazione ha successo.
     */
    boolean performAuthentication(AuthData data);
}
