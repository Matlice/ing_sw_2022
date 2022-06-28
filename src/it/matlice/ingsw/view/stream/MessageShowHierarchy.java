package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.Settings;

import java.util.StringJoiner;

public class MessageShowHierarchy extends AStreamMessage {

    private final Hierarchy hierarchy;

    public MessageShowHierarchy(StreamView view, Hierarchy hierarchy) {
        super(view);
        this.hierarchy = hierarchy;
    }

    @Override
    public String getMessage() {
         return this.hierarchy.getRootCategory().toString(); // todo togli toString()
    }
}
