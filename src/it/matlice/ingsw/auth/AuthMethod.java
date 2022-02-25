package it.matlice.ingsw.auth;

public interface AuthMethod {
    boolean performAuthentication(AuthData data);
}
