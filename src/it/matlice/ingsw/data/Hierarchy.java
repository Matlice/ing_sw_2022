package it.matlice.ingsw.data;

public abstract class Hierarchy {
    private final Category rootCategory;

    public Hierarchy(Category rootCategory) {
        this.rootCategory = rootCategory;
    }

    public Category getRootCategory() {
        return rootCategory;
    }
}
