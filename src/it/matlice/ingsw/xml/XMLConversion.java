package it.matlice.ingsw.xml;

import it.matlice.ingsw.tree.MapNode;

import java.util.TreeMap;

public interface XMLConversion {
    /**
     * converts a given node to a java object representation
     *
     * @param node     the current node
     * @param children a TreeMap of already converted node children
     * @return the converted object
     */
    Object convert(XMLNode node, TreeMap<String, Object> children, XMLNode parent, MapNode dinasty);
}
