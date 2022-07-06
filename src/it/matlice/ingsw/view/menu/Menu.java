package it.matlice.ingsw.view.menu;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.*;

public class Menu {

    private final SortedSet<MenuEntry> entries = new TreeSet<>();
    private final HashMap<Integer, MenuEntry> call_ref = new HashMap<>();
    private String prompt = null;
    private MenuEntry last_entry;

    /**
     * sets the ask prompt value
     *
     * @param prompt the string to be prompted
     * @return this
     */
    public Menu setPrompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    /**
     * adds an entry to the menu. position will be assigned incrementally based oin the number of elements in the menu
     *
     * @param entryWrapper
     * @return this
     */
    public <T> Menu addEntry(MenuEntryWrapper<T> entryWrapper) {
        return this.addEntry(
                new MenuEntry(
                        entryWrapper.getName(),
                        entryWrapper.getPosition(this.entries.size()),
                        entryWrapper.getIndex(this.call_ref.size() == 0 ? 0 : Collections.max(this.call_ref.keySet()) + 1),
                        entryWrapper.getAction(),
                        null,
                        entryWrapper.isDisabled()
                )
        );
    }

    /**
     * adds an entry to the menu
     *
     * @param entry the entry
     * @return this
     */
    public Menu addEntry(MenuEntry entry) {
        if (this.call_ref.containsKey(entry.getCall_index())) {
            System.err.println("Duplicate position in menu " + entry.getCall_index());
            return null;
        }
        this.entries.add(entry);
        this.call_ref.put(entry.getCall_index(), entry);
        this.last_entry = entry;
        return this;
    }

    /**
     * Sets the last added entry's payload
     *
     * @param payload the object to be passed
     * @return this
     */
    public Menu payload(Object payload) {
        if (this.last_entry == null) return this;
        this.last_entry.setPayload(payload);
        return this;
    }

    /**
     * Sets the state of the last added entry
     *
     * @param disable the state to be assumed
     * @return this
     */
    public Menu disable(boolean disable) {
        if (this.last_entry == null) return this;
        this.last_entry.setDisabled(disable);
        return this;
    }

    /**
     * Gets the reference to a MenuEntry
     *
     * @param call_reference the integer to be typed to load that entry
     * @return the entry reference or null if inexistent
     */
    public MenuEntry getEntry(int call_reference) {
        for (var e : this.entries)
            if (e.getCall_index() == call_reference) return e;
        return null;
    }

    /**
     * displays the menu on the output stream
     *
     * @param in  input stream where to get user input
     * @param out output stream to print data
     * @return the result of the call to {@link MenuAction}
     */
    public Object displayOnce(Scanner in, PrintStream out) {
        this.onlyDisplay(out);
        return this.selectAfterDisplay(in, out);
    }

    /**
     * displays the menu on the output stream until the user inserts a valid answer
     *
     * @param in  input stream where to get user input
     * @param out output stream to print data
     * @return the result of the call to {@link MenuAction}
     */
    public Object display(Scanner in, PrintStream out) {
        this.onlyDisplay(out);
        Object r = null;
        while ((r = this.selectAfterDisplay(in, out)) == null) out.println("Errore di scelta");
        return r;
    }

    /**
     * shows the menu without asking for any input. input should be managed by the user
     *
     * @param out
     */
    public void onlyDisplay(PrintStream out) {
        if (this.prompt != null) out.println("\n" + this.prompt);

        for (MenuEntry e : this.entries)
            out.println(e.toString().replaceAll("\n", "\n\t"));
    }

    /**
     * asks for an entry of the menu
     *
     * @param in
     * @param out
     * @return the return of the action lambda
     */
    public Object selectAfterDisplay(@NotNull Scanner in, @NotNull PrintStream out) {
        int action;
        try {
            action = in.nextInt();
            in.nextLine();
        } catch (InputMismatchException e) {
            in.nextLine();
            return null;
        }
        if (!this.call_ref.containsKey(action))
            return null;
        var ref = this.call_ref.get(action);
        if (ref.isDisabled()) return null;
        return ref.getAction().onUserSelect(in, out, ref);
    }
}