package it.matlice.ingsw.auth;

import java.util.List;

/**
 * L'interfaccia rappresenta una classe in grado di fornire una lista di AuthMethod
 * in grado di fornire l'autenticazione.
 */
public interface Authenticable {

    /**
     * Fornisce una lista di metodi di autenticazione in grado di autenticare la classe.
     *
     * @return una lista di istanze di metodi di autenticazione.
     */
    List<AuthMethod> getAuthMethods();
}
