package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Hierarchy;

public class StreamMessageShowHierarchy extends AStreamMessage {

    private final Hierarchy hierarchy;

    public StreamMessageShowHierarchy(StreamView view, Hierarchy hierarchy) {
        super(view);
        this.hierarchy = hierarchy;
    }

    @Override
    public String getMessage() {
         return this.hierarchy.getRootCategory().getStreamRepresentation();
    }
}
