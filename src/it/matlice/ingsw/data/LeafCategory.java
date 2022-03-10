package it.matlice.ingsw.data;

/**
 * rappresenta una categoria di tipo foglia.
 **/
public abstract class LeafCategory extends Category {

    public abstract NodeCategory convertToNode();

}
