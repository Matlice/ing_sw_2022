package it.matlice.ingsw.xml;

import java.util.HashMap;

public class XMLNode {
    private final String name;
    private final HashMap<String, String> attributes = new HashMap<>();
    private String value;

    /**
     * Constructor of XMLNode
     *
     * @param name the node name
     */
    public XMLNode(String name) {
        this.name = name;
    }

    /**
     * Adds an attribute to the HashMap of the class
     *
     * @param name
     * @param value
     */
    public void add_attribute(String name, String value) {
        attributes.put(name, value);
    }

    public String get_attribute(String name) {
        return attributes.get(name);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        var r = new StringBuilder("<" + name);
        attributes.forEach((k, v) -> r.append(String.format(" %s=\"%s\"", k, v)));
        r.append(">").append(value).append("</").append(name).append(">");
        return r.toString();
    }
}
