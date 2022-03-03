package it.matlice.ingsw.view.stream;


public class MenuEntry implements Comparable<MenuEntry> {

    private final String name;
    private final int pos_index;
    private final int call_index;
    private final MenuAction action;
    private boolean disabled;
    private Object payload;

    public MenuEntry(String name, int pos_index, int call_index, MenuAction action, Object payload, boolean disabled) {
        this.name = name;
        this.pos_index = pos_index;
        this.call_index = call_index;
        if (action != null) this.action = action;
        else this.action = (in, out, ref) -> call_index;
        this.payload = payload;
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Object getPayload() {
        return this.payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public int compareTo(MenuEntry o) {
        //this < o --> -1
        //this == o --> 0
        //this > o --> 1

        if (this.pos_index == o.pos_index) {
            if (this != o) System.err.println("Found duplicate entry " + this.pos_index);
            return 0;
        }
        if (this.pos_index >= 0 && o.pos_index < 0) return -1;
        if (this.pos_index < 0 && o.pos_index >= 0) return 1;
        return Integer.compare(this.pos_index, o.pos_index);
    }

    public String getName() {
        return this.name;
    }

    public int getPos_index() {
        return this.pos_index;
    }

    public int getCall_index() {
        return this.call_index;
    }

    public MenuAction getAction() {
        return this.action;
    }

    @Override
    public String toString() {
        return " " + (this.disabled ? "X" : this.call_index) + ") " + this.name + (this.disabled ? " " + "DISABLED" : "");
    }
}