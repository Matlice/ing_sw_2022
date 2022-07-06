package it.matlice.ingsw.model.data;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * rappresenta una categoria di tipo foglia.
 **/
public abstract class LeafCategory extends Category {

    @Override
    public boolean isCategoryValid() {
        return true;
    }

    @Override
    public List<List<Category>> getChildrenPath(@NotNull List<List<Category>> acc, List<Category> prefix, boolean addNodes){
        List<Category> p = new LinkedList<>(prefix);
        p.add(this);
        acc.add(p);
        return acc;
    }
}
