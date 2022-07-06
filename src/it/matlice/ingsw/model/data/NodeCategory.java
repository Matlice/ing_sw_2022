package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.data.impl.jdbc.types.NodeCategoryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * rappresenta una categoria di tipo nodo.
 */
public abstract class NodeCategory extends Category {
    private final List<Category> children = new ArrayList<>();

    /**
     * aggiunge una categoria tra i propri figli.
     *
     * @param child categoria figlio
     * @return la categoria aggiunta
     */
    public Category addChild(@NotNull Category child) {
        child.setFather(this);
        this.children.add(child);
        return child;
    }

    /**
     * rimuove un figlio
     *
     * @param child figlio da rimuovere
     * @return il figlio rimosso
     */
    public Category removeChild(@NotNull Category child) {
        child.setFather(null);
        this.children.remove(child);
        return child;
    }

    public Category[] getChildren() {
        return this.children.toArray(Category[]::new);
    }

    /**
     * Returns a shallow copy of this {@code HashMap} instance: the keys and
     * values themselves are not cloned.
     *
     * @return a shallow copy of this map
     */
    @Override
    public abstract Object clone();

    @Override
    public NodeCategory convertToNode(){
        return this;
    }

    @Override
    public boolean isCategoryValid() {
        return this.getChildren().length >= 2 && Arrays.stream(this.getChildren()).allMatch(Category::isCategoryValid);
    }

    @Override
    public List<List<Category>> getChildrenPath(@NotNull List<List<Category>> acc, List<Category> prefix, boolean addNodes){
        List<Category> p = new LinkedList<>(prefix);
        p.add(this);
        if (addNodes) acc.add(p);
        for (var child : this.getChildren())
            child.getChildrenPath(acc, p, addNodes);
        return acc;
    }
}
