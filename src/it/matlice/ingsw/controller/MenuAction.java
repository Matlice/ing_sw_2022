package it.matlice.ingsw.controller;

import it.matlice.ingsw.model.data.User;

public class MenuAction<T> {

    private final MenuType type;
    private final ReturnAction<T> action;
    private final boolean disabled;
    private final Integer index;
    private final Integer position;

    public MenuAction(MenuType type, ReturnAction<T> action) {
        this(type, action, false, null, null);
    }

    public MenuAction(MenuType type, ReturnAction<T> action, boolean disabled) {
        this(type, action, disabled, null, null);
    }

    public MenuAction(MenuType type, ReturnAction<T> action, boolean disabled, Integer index, Integer position) {
        this.type = type;
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

    public Integer getPosition() {
        return this.position;
    }

    public MenuType getType(){
        return this.type;
    }

    public ReturnAction<T> getAction() {
        return this.action;
    }

    public boolean isPermitted(User u) {
        return true; //nessuna limitazione in questo tipo di scelta
    }
}
