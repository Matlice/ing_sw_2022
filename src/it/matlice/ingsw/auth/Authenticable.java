package it.matlice.ingsw.auth;

public interface Authenticable {
    boolean authenticate(AuthMethod method, AuthData data);
    AuthMethod[] getAuthMethods();
}
