package it.matlice.ingsw.controller;

import it.matlice.ingsw.data.User;

public class MenuAction<T> {

    private final String name;
    private final Class<? extends User> requestedUserType;
    private final ReturnAction<T> action;
    private final boolean disabled;
    private final Integer index;
    private final Integer position;

    public MenuAction(String name, Class<? extends User> requestedUserType, ReturnAction<T> action) {
        this(name, requestedUserType, action, false, null, null);
    }

    public MenuAction(String name, Class<? extends User> requestedUserType, ReturnAction<T> action, boolean disabled) {
        this(name, requestedUserType, action, disabled, null, null);
    }

    public MenuAction(String name, Class<? extends User> requestedUserType, ReturnAction<T> action, boolean disabled, Integer index, Integer position) {
        this.name = name;
        this.requestedUserType = requestedUserType;
        this.action = action;
        this.disabled = disabled;
        this.index = index;
        this.position = position;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public Integer getIndex() {
        return this.index;
    }

    public int getPosition() {
        return this.position;
    }

    public String getName() {
        return this.name;
    }

    public Class<? extends User> getRequestedUserType() {
        return this.requestedUserType;
    }

    public ReturnAction<T> getAction() {
        return this.action;
    }

    public boolean isPermitted(User u) {
        return this.requestedUserType.isInstance(u);
    }
}
