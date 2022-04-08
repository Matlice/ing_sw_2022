package it.matlice.ingsw.tree;

import javax.xml.stream.XMLStreamException;

public interface TreeAction<T> {
    void nodeAction(Node<T> ref);
}
