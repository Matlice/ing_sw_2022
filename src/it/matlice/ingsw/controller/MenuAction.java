package it.matlice.ingsw.controller;

import it.matlice.ingsw.model.data.User;

import java.util.Arrays;

public class MenuAction<T> {

    private final String name;
    private final ReturnAction<T> action;
    private final boolean disabled;
    private final Integer index;
    private final Integer position;

    public MenuAction(String name, ReturnAction<T> action) {
        this(name, action, false, null, null);
    }

    public MenuAction(String name, ReturnAction<T> action, boolean disabled) {
        this(name, action, disabled, null, null);
    }

    public MenuAction(String name, ReturnAction<T> action, boolean disabled, Integer index, Integer position) {
        this.name = name;
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

    public ReturnAction<T> getAction() {
        return this.action;
    }

    public boolean isPermitted(User u) {
        return true; //nessuna limitazione in questo tipo di scelta
    }
}
