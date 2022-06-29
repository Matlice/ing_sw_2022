
package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.impl.jdbc.XMLImport;

import java.util.StringJoiner;

public class StreamMessageHierarchyImportFullfilled extends AStreamMessage {

    private final XMLImport.HierarchyXML category;

    public StreamMessageHierarchyImportFullfilled(StreamView view, XMLImport.HierarchyXML category) {
        super(view);
        this.category = category;
    }

    @Override
    public String getMessage() {
        return String.format("Gerarchia %s importata correttamente!", this.category.root.name);
    }
}
