package it.matlice.ingsw.model;

import it.matlice.ingsw.data.User;

public class MenuAction {

    private final String name;
    private final Class<? extends User> requestedUserType;
    private final BooleanAction action;

    public MenuAction(String name, Class<? extends User> requestedUserType, BooleanAction action) {
        this.name = name;
        this.requestedUserType = requestedUserType;
        this.action = action;
    }

    public String getName() {
        return this.name;
    }

    public Class<? extends User> getRequestedUserType() {
        return this.requestedUserType;
    }

    public BooleanAction getAction() {
        return this.action;
    }

    public boolean isPermitted(User u) {
        return true || u.getClass().isAssignableFrom(this.requestedUserType);
    }
}
