package it.matlice.ingsw.model.data;

/**
 * rappresenta una categoria di tipo foglia.
 **/
public abstract class LeafCategory extends Category {

    public abstract NodeCategory convertToNode();

}
