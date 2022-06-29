package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.NodeCategory;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class StreamHierarchyAdapter implements StreamDataType {

    private Hierarchy hierarchy;
    public StreamHierarchyAdapter(Hierarchy o) {
        this.hierarchy = o;
    }

    @Override
    public String getStreamRepresentation() {
        return this.hierarchy.getRootCategory().getName();
    }
}
