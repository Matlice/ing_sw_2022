package it.matlice.ingsw.auth.password;

import it.matlice.ingsw.auth.AuthData;

public class PasswordAuthData implements AuthData {

    private String password;

    public PasswordAuthData(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
