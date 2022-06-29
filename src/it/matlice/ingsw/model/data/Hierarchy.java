package it.matlice.ingsw.model.data;

import it.matlice.ingsw.view.stream.StreamRepresentable;

/**
 * la classe rappresenta una gerarchia.
 * il nome della gerarchia è dato dal nome della sua categoria radice.
 */
public abstract class Hierarchy implements StreamRepresentable {
    private final Category rootCategory;

    public Hierarchy(Category rootCategory) {
        this.rootCategory = rootCategory;
    }

    public Category getRootCategory() {
        return this.rootCategory;
    }

    public String getStreamRepresentation(){
        return this.rootCategory.getName();
    }
}
