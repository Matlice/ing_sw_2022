package it.matlice.ingsw.auth.password;

import it.matlice.ingsw.auth.Authenticable;

public interface PasswordAuthenticable extends Authenticable {
    String getPasswordHash();

    String getPasswordSalt();

    void setPassword();
}
