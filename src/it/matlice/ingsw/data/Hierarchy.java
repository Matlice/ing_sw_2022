package it.matlice.ingsw.data;

/**
 * la classe rappresenta una gerarchia.
 * il nome della gerarchia è dato dal nome della sua categoria radice.
 */
public abstract class Hierarchy {
    private final Category rootCategory;

    public Hierarchy(Category rootCategory) {
        this.rootCategory = rootCategory;
    }

    public Category getRootCategory() {
        return this.rootCategory;
    }
}
