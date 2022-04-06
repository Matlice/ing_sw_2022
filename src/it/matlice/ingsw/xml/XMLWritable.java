package it.matlice.ingsw.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public interface XMLWritable {

    default void toXML(XMLStreamWriter xmlw) throws XMLStreamException {
        this.toXML(xmlw, null);
    }

    void toXML(XMLStreamWriter xmlw, Object data) throws XMLStreamException;
}
