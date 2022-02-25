package it.matlice.ingsw.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class NodeCategory extends Category {
    private final List<Category> children = new ArrayList<>();

    public Category addChild(Category child){
        child.setFather(this);
        children.add(child);
        return child;
    }

    public Category removeChild(Category child){
        child.setFather(null);
        children.remove(child);
        return child;
    }

    public Category[] getChildren(){
        return children.toArray(Category[]::new);
    }
}
