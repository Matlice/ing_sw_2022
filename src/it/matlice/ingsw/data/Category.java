package it.matlice.ingsw.data;

import java.util.HashMap;

public abstract class Category extends HashMap<String, TypeDefinition<?>> {

    private Category father = null;

    public Category getFather() {
        return father;
    }

    public void setFather(Category father) {
        this.father = father;
    }

    public boolean isRoot(){
        return father == null;
    }

    public abstract String getName();
}
