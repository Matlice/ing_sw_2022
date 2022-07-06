package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.auth.AuthMethod;

import java.util.List;

public class NullUser extends User{
    @Override
    public String getUsername() {
        return "--";
    }

    @Override
    public Long getLastLoginTime() {
        return 0L;
    }

    @Override
    public void setLastLoginTime(long time) {}

    @Override
    public List<AuthMethod> getAuthMethods() {
        return List.of();
    }

    @Override
    public List<UserTypes> getAuthTypes() {
        return List.of();
    }

    @Override
    public Boolean isAdmin() {
        return false;
    }
}
