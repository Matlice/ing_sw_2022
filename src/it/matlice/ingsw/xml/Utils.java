package it.matlice.ingsw.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

public class Utils {

    public static final XMLConversion StringConverter = ((node, children, parent, dinasty) -> node.getValue());
    public static final XMLConversion ArrayListConverter = (node, children, parent, dinasty) -> new ArrayList<>(children.values());
    public static final XMLConversion LinkedListConverter = (node, children, parent, dinasty) -> new LinkedList<>(children.values());
    public static final XMLConversion MapConverter = ((node, children, parent, dinasty) -> children.values());
    public static final XMLConversion NullConverter = ((node, children, parent, dinasty) -> null);
    public static final XMLConversion IntegerConverter = ((node, children, parent, dinasty) -> Integer.parseInt(node.getValue()));
    public static final XMLConversion ShortConverter = ((node, children, parent, dinasty) -> Short.parseShort(node.getValue()));
    public static final XMLConversion DoubleConverter = ((node, children, parent, dinasty) -> Double.parseDouble(node.getValue()));
    public static final XMLConversion BooleanConverter = ((node, children, parent, dinasty) -> Boolean.parseBoolean(node.getValue()));


    public static XMLConversion AdvBooleanConverter(List<String> trueValues) {
        return ((node, children, parent, dinasty) -> trueValues.contains(node.getValue().toLowerCase()));
    }

    /**
     * Method that writes in an XML file a String
     *
     * @param w          XMLStreamWriter
     * @param text       the text in the string
     * @param name       the name of the string
     * @param attributes the attributes of the string
     * @throws XMLStreamException
     */
    public static void XMLWriteString(XMLStreamWriter w, String text, String name, Map<String, String> attributes) throws XMLStreamException {
        w.writeStartElement(name);
        if (attributes != null)
            for (String k : attributes.keySet())
                w.writeAttribute(k, attributes.get(k));
        w.writeCharacters(text);
        w.writeEndElement();
    }

    /**
     * Method that writes in an XML file a List
     *
     * @param w          XMLStreamWriter
     * @param array      the list which implements XMLWritable
     * @param name       the name of the string
     * @param attributes the attributes of the string
     * @throws XMLStreamException
     */
    public static void XMLWriteList(XMLStreamWriter w, List<? extends XMLWritable> array, String name, Map<String, String> attributes) throws XMLStreamException {
        w.writeStartElement(name);
        if (attributes != null)
            for (String k : attributes.keySet())
                w.writeAttribute(k, attributes.get(k));

        for (int i = 0; i < array.size(); i++)
            array.get(i).toXML(w, i);
        w.writeEndElement();
    }

}
