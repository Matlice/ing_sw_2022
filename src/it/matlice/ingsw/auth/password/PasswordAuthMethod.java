package it.matlice.ingsw.auth.password;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.AuthMethod;

public class PasswordAuthMethod implements AuthMethod {

    private PasswordAuthenticable user;

    public PasswordAuthMethod(PasswordAuthenticable user) {
        this.user = user;
    }

    @Override
    public boolean performAuthentication(AuthData data) {
        assert data instanceof PasswordAuthData;
        return ((PasswordAuthData) data).getPassword().equals(user.getPasswordHash()); //todo
    }
}
