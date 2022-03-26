package it.matlice.ingsw.model.auth.password;

import it.matlice.ingsw.model.auth.AuthData;

/**
 * Questa classe rappresenta i dati necessari per l'autenticazione con password.
 */
public record PasswordAuthData(String password) implements AuthData {
}
