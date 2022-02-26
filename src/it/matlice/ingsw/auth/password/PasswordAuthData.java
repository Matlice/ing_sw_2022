package it.matlice.ingsw.auth.password;

import it.matlice.ingsw.auth.AuthData;

/**
 * Questa classe rappresenta i dati necessari per l'autenticazione con password.
 */
public record PasswordAuthData(String password) implements AuthData {
    public String getPassword() {
        return password;
    }
}
