package it.matlice.ingsw.controller;

import it.matlice.ingsw.model.data.User;

import java.util.Arrays;

public class PrivilegedMenuAction<T> extends MenuAction<T>{

    private User.UserTypes[] allowedTypes;


    public PrivilegedMenuAction(String name, User.UserTypes[] allowedTypes, ReturnAction<T> action) {
        super(name, action);
        this.allowedTypes = allowedTypes;
    }

    public PrivilegedMenuAction(String name, User.UserTypes[] allowedTypes, ReturnAction<T> action, boolean disabled) {
        super(name, action, disabled);
        this.allowedTypes = allowedTypes;
    }

    public PrivilegedMenuAction(String name, User.UserTypes[] allowedTypes, ReturnAction<T> action, boolean disabled, Integer index, Integer position) {
        super(name, action, disabled, index, position);
        this.allowedTypes = allowedTypes;
    }

    @Override
    public boolean isPermitted(User u) {
        return Arrays.stream(this.allowedTypes).anyMatch(e -> u.getAuthTypes().contains(e));
    }
}
