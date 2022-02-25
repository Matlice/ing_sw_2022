package it.matlice.ingsw.controller;

import it.matlice.ingsw.data.User;

public interface Authentication {
    User getUser();
    long getLoginTime();
    long getExpirationTime();
    boolean isExpired();
}
