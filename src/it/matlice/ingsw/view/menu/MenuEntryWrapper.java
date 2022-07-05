package it.matlice.ingsw.view.menu;
import it.matlice.ingsw.controller.MenuType;
import it.matlice.ingsw.model.data.User;

public class MenuEntryWrapper {

    private final String name;
    private final MenuAction action;
    private final boolean disabled;
    private final Integer index;
    private final Integer position;

    public MenuEntryWrapper(String name, MenuAction action) {
        this(name, action, false, null, null);
    }

    public MenuEntryWrapper(String name, MenuAction action, boolean disabled) {
        this(name, action, disabled, null, null);
    }

    public MenuEntryWrapper(String name, MenuAction action, boolean disabled, Integer index, Integer position) {
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

    public String getName(){
        return this.name;
    }

    public MenuAction getAction() {
        return this.action;
    }
}

