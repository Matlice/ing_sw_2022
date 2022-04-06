package it.matlice.ingsw.xml;

import it.matlice.ingsw.tree.MapNode;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Stack;
import java.util.TreeMap;

public class XMLParser {

    private final XMLStreamReader reader;
    private final TreeMap<String, XMLConversion> conversion_map = new TreeMap<>();
    private MapNode<Object> root = null;

    /**
     * Constructor of the class
     *
     * @param file Name of file
     * @throws XMLStreamException
     * @throws FileNotFoundException
     */
    public XMLParser(String file) throws XMLStreamException, FileNotFoundException {
        this(new FileInputStream(file));
    }

    /**
     * Constructor of the class
     *
     * @param file The file
     * @throws XMLStreamException
     * @throws FileNotFoundException
     */
    public XMLParser(File file) throws XMLStreamException, FileNotFoundException {
        this(new FileInputStream(file));
    }

    /**
     * Constructor of the class
     *
     * @param file a FileInputStream
     * @throws XMLStreamException
     */
    public XMLParser(FileInputStream file) throws XMLStreamException {
        reader = XMLInputFactory.newInstance().createXMLStreamReader(file);
    }

    /**
     * returns the current node
     *
     * @return current node
     */
    private XMLNode getNode() {
        XMLNode element = new XMLNode(reader.getLocalName());
        for (int i = 0; i < reader.getAttributeCount(); i++)
            element.add_attribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        element.setValue(null);
        return element;
    }

    /**
     * adds an XMLConversion to conversion-map attribute
     *
     * @param tag_name
     * @param conv_fnc
     * @return this
     */
    public XMLParser add_conversion(String tag_name, XMLConversion conv_fnc) {
        conversion_map.put(tag_name, conv_fnc);
        return this;
    }

    /**
     * Parser of a XML file
     *
     * @param out PrintStream object
     * @return dta from an {@link MapNode}
     * @throws XMLStreamException
     */
    public Object parse(PrintStream out) throws XMLStreamException {
        Stack<MapNode<Object>> last = new Stack<>();
        while (reader.hasNext()) {
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    if (root == null) {
                        root = new MapNode<>(getNode());
                        last.push(root);
                    } else
                        last.push(last.peek().addChild(reader.getLocalName(), getNode()));
                    break;
                case XMLStreamConstants.CHARACTERS:
                    ((XMLNode) last.peek().getData()).setValue(reader.getText().trim());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    last.pop();
            }
            reader.next();
        }

        root.reverse_BFT((r) -> {
            var node = (XMLNode) r.getData();
            if (conversion_map.containsKey(node.getName()))
                r.setData(conversion_map.get(node.getName()).convert((XMLNode) r.getData(), ((MapNode<Object>) r).getChildrenMap(), r.getParent() != null ? (XMLNode) r.getParent().getData() : null, (MapNode) r));
            else
                throw new XMLStreamException();
        });
        reader.close();
        return root.getData();
    }
}
