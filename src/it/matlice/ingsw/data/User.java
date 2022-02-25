package it.matlice.ingsw.data;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.AuthMethod;
import it.matlice.ingsw.auth.Authenticable;

public abstract class User implements Authenticable {
    public abstract String getUsername();

    public abstract boolean authenticate(AuthMethod method, AuthData authdata);
    public abstract AuthMethod[] getAuthMethods();
}
