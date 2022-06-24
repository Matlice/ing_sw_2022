package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.Authentication;

public class NullAuthentication implements Authentication {
    @Override
    public User getUser() {
        return new NullUser();
    }

    @Override
    public long getLoginTime() {
        return 0;
    }

    @Override
    public long getExpirationTime() {
        return 0;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String loginProblem() {
        return "";
    }
}
