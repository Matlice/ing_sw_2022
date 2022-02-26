package it.matlice.ingsw.data;

import java.util.ArrayList;
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
    public Category addChild(Category child) {
        child.setFather(this);
        this.children.add(child);
        //todo lanciare un errore se il campo è duplicato?
        return child;
    }

    /**
     * rimuove un figlio
     *
     * @param child figlio da rimuovere
     * @return il figlio rimosso
     */
    public Category removeChild(Category child) {
        child.setFather(null);
        this.children.remove(child);
        return child;
    }

    public Category[] getChildren() {
        return this.children.toArray(Category[]::new);
    }
}
