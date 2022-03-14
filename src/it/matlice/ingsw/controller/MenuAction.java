package it.matlice.ingsw.model;

import it.matlice.ingsw.data.User;

public class MenuAction<T> {

    private final String name;
    private final Class<? extends User> requestedUserType;
    private final ReturnAction<T> action;
    private final boolean disabled;

    public MenuAction(String name, Class<? extends User> requestedUserType, ReturnAction<T> action) {
        this(name, requestedUserType, action, false);
    }

    public MenuAction(String name, Class<? extends User> requestedUserType, ReturnAction<T> action, boolean disabled) {
        this.name = name;
        this.requestedUserType = requestedUserType;
        this.action = action;
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return this.disabled;
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
