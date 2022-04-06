package it.matlice.ingsw.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * {@link XMLStreamWriter} implementation with custom options, such as having newlines after tags with the option to choose which ones shouldn't have the newline
 */
public class PrettyXMLStreamWriter implements XMLStreamWriter, AutoCloseable {

    public static final String ENCODING_UTF_8 = "utf-8";
    public static final String ENCODING_UTF_16 = "utf-16";
    public static final String XML_VERSION_1_0 = "1.0";

    private final XMLStreamWriter writer;
    private final Stack<String> tagStack = new Stack<>();
    private final List<String> noNewLine;

    private int level = 0;

    /**
     * Constructor for {@link PrettyXMLStreamWriter}, it gives the possibility yo choose which tags shouldn't have the newline after them with the noNewLine param
     *
     * @param filename  filename of the file to write to
     * @param noNewLine String[] of tags, these tags won't have the newline after them (their CHARS and their end-tags too)
     * @throws FileNotFoundException
     * @throws XMLStreamException
     */
    public PrettyXMLStreamWriter(String filename, String[] noNewLine, String encoding, String version) throws FileNotFoundException, XMLStreamException {
        this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(filename), ENCODING_UTF_8);
        this.noNewLine = Arrays.asList(noNewLine);
        this.writeStartDocument(encoding, version);
    }

    public PrettyXMLStreamWriter(String filename, String[] noNewLine) throws FileNotFoundException, XMLStreamException {
        this(filename, noNewLine, ENCODING_UTF_8, XML_VERSION_1_0);
    }

    /**
     * Constructor for {@link PrettyXMLStreamWriter}, by default all tags will have the newline after them
     *
     * @param filename filename of the file to write to
     * @throws FileNotFoundException
     * @throws XMLStreamException
     */
    public PrettyXMLStreamWriter(String filename) throws FileNotFoundException, XMLStreamException {
        this(filename, new String[]{});
    }

    /**
     * Writes a start tag to the output.  All writeStartElement methods
     * open a new scope in the internal namespace context.  Writing the
     * corresponding EndElement causes the scope to be closed.
     *
     * @param localName local name of the tag, may not be null
     * @throws XMLStreamException
     */
    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
        try {
            if (!noNewLine.contains(tagStack.peek())) {
                writer.writeCharacters("\n");
                for (int i = 0; i < level; i++) writer.writeCharacters("\t");
            }
        } catch (EmptyStackException e) {
            writer.writeCharacters("\n");
        }
        tagStack.push(localName);
        writer.writeStartElement(localName);
        level++;
    }

    /**
     * Writes a start tag to the output
     *
     * @param namespaceURI the namespaceURI of the prefix to use, may not be null
     * @param localName    local name of the tag, may not be null
     * @throws XMLStreamException if the namespace URI has not been bound to a prefix and
     *                            javax.xml.stream.isRepairingNamespaces has not been set to true
     */
    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        try {
            if (!noNewLine.contains(tagStack.peek())) {
                writer.writeCharacters("\n");
                for (int i = 0; i < level; i++) writer.writeCharacters("\t");
            }
        } catch (EmptyStackException e) {
            writer.writeCharacters("\n");
        }
        tagStack.push(localName);
        writer.writeStartElement(namespaceURI, localName);
        level++;
    }

    /**
     * Writes a start tag to the output
     *
     * @param localName    local name of the tag, may not be null
     * @param prefix       the prefix of the tag, may not be null
     * @param namespaceURI the uri to bind the prefix to, may not be null
     * @throws XMLStreamException
     */
    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        try {
            if (!noNewLine.contains(tagStack.peek())) {
                writer.writeCharacters("\n");
                for (int i = 0; i < level; i++) writer.writeCharacters("\t");
            }
        } catch (EmptyStackException e) {
            writer.writeCharacters("\n");
        }
        tagStack.push(localName);
        writer.writeStartElement(prefix, localName, namespaceURI);
        level++;
    }

    /**
     * Writes an empty element tag to the output
     *
     * @param namespaceURI the uri to bind the tag to, may not be null
     * @param localName    local name of the tag, may not be null
     * @throws XMLStreamException if the namespace URI has not been bound to a prefix and
     *                            javax.xml.stream.isRepairingNamespaces has not been set to true
     */
    @Override
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        writer.writeCharacters("\n");
        for (int i = 0; i < level; i++) writer.writeCharacters("\t");
        writer.writeEmptyElement(namespaceURI, localName);
    }

    /**
     * Writes an empty element tag to the output
     *
     * @param prefix       the prefix of the tag, may not be null
     * @param localName    local name of the tag, may not be null
     * @param namespaceURI the uri to bind the tag to, may not be null
     * @throws XMLStreamException
     */
    @Override
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        writer.writeCharacters("\n");
        for (int i = 0; i < level; i++) writer.writeCharacters("\t");
        writer.writeEmptyElement(prefix, localName, namespaceURI);
    }

    /**
     * Writes an empty element tag to the output
     *
     * @param localName local name of the tag, may not be null
     * @throws XMLStreamException
     */
    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException {
        writer.writeCharacters("\n");
        for (int i = 0; i < level; i++) writer.writeCharacters("\t");
        writer.writeEmptyElement(localName);
    }

    /**
     * Writes an end tag to the output relying on the internal
     * state of the writer to determine the prefix and local name
     * of the event.
     *
     * @throws XMLStreamException
     */
    @Override
    public void writeEndElement() throws XMLStreamException {
        level--;
        if (!noNewLine.contains(tagStack.pop())) {
            writer.writeCharacters("\n");
            for (int i = 0; i < level; i++) writer.writeCharacters("\t");
        }
        writer.writeEndElement();
    }

    /**
     * Closes any start tags and writes corresponding end tags.
     *
     * @throws XMLStreamException
     */
    @Override
    public void writeEndDocument() throws XMLStreamException {
        writer.writeEndDocument();
    }

    /**
     * Close this writer and free any resources associated with the
     * writer.  This must not close the underlying output stream.
     *
     * @throws XMLStreamException
     */
    @Override
    public void close() throws XMLStreamException {
        this.writeEndDocument();
        this.flush();
        writer.close();
    }

    /**
     * Write any cached data to the underlying output mechanism.
     *
     * @throws XMLStreamException
     */
    @Override
    public void flush() throws XMLStreamException {
        writer.flush();
    }

    /**
     * Writes an attribute to the output stream without
     * a prefix.
     *
     * @param localName the local name of the attribute
     * @param value     the value of the attribute
     * @throws IllegalStateException if the current state does not allow Attribute writing
     * @throws XMLStreamException
     */
    @Override
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        writer.writeAttribute(localName, value);
    }

    /**
     * Writes an attribute to the output stream
     *
     * @param prefix       the prefix for this attribute
     * @param namespaceURI the uri of the prefix for this attribute
     * @param localName    the local name of the attribute
     * @param value        the value of the attribute
     * @throws IllegalStateException if the current state does not allow Attribute writing
     * @throws XMLStreamException    if the namespace URI has not been bound to a prefix and
     *                               javax.xml.stream.isRepairingNamespaces has not been set to true
     */
    @Override
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        writer.writeAttribute(prefix, namespaceURI, localName, value);
    }

    /**
     * Writes an attribute to the output stream
     *
     * @param namespaceURI the uri of the prefix for this attribute
     * @param localName    the local name of the attribute
     * @param value        the value of the attribute
     * @throws IllegalStateException if the current state does not allow Attribute writing
     * @throws XMLStreamException    if the namespace URI has not been bound to a prefix and
     *                               javax.xml.stream.isRepairingNamespaces has not been set to true
     */
    @Override
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        writer.writeAttribute(namespaceURI, localName, value);
    }

    /**
     * Writes a namespace to the output stream
     * If the prefix argument to this method is the empty string,
     * "xmlns", or null this method will delegate to writeDefaultNamespace
     *
     * @param prefix       the prefix to bind this namespace to
     * @param namespaceURI the uri to bind the prefix to
     * @throws IllegalStateException if the current state does not allow Namespace writing
     * @throws XMLStreamException
     */
    @Override
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        writer.writeNamespace(prefix, namespaceURI);
    }

    /**
     * Writes the default namespace to the stream
     *
     * @param namespaceURI the uri to bind the default namespace to
     * @throws IllegalStateException if the current state does not allow Namespace writing
     * @throws XMLStreamException
     */
    @Override
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        writer.writeDefaultNamespace(namespaceURI);
    }

    /**
     * Writes an xml comment with the data enclosed
     *
     * @param data the data contained in the comment, may be null
     * @throws XMLStreamException
     */
    @Override
    public void writeComment(String data) throws XMLStreamException {
        writer.writeComment(data);
    }

    /**
     * Writes a processing instruction
     *
     * @param target the target of the processing instruction, may not be null
     * @throws XMLStreamException
     */
    @Override
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        writer.writeProcessingInstruction(target);
    }

    /**
     * Writes a processing instruction
     *
     * @param target the target of the processing instruction, may not be null
     * @param data   the data contained in the processing instruction, may not be null
     * @throws XMLStreamException
     */
    @Override
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        writer.writeProcessingInstruction(target, data);
    }

    /**
     * Writes a CData section
     *
     * @param data the data contained in the CData Section, may not be null
     * @throws XMLStreamException
     */
    @Override
    public void writeCData(String data) throws XMLStreamException {
        writer.writeCData(data);
    }

    /**
     * Write a DTD section.  This string represents the entire doctypedecl production
     * from the XML 1.0 specification.
     *
     * @param dtd the DTD to be written
     * @throws XMLStreamException
     */
    @Override
    public void writeDTD(String dtd) throws XMLStreamException {
        writer.writeDTD(dtd);
    }

    /**
     * Writes an entity reference
     *
     * @param name the name of the entity
     * @throws XMLStreamException
     */
    @Override
    public void writeEntityRef(String name) throws XMLStreamException {
        writer.writeEntityRef(name);
    }

    /**
     * Write the XML Declaration. Defaults the XML version to 1.0, and the encoding to utf-8
     *
     * @throws XMLStreamException
     */
    @Override
    public void writeStartDocument() throws XMLStreamException {
        writer.writeStartDocument();
    }

    /**
     * Write the XML Declaration. Defaults the XML version to 1.0
     *
     * @param version version of the xml document
     * @throws XMLStreamException
     */
    @Override
    public void writeStartDocument(String version) throws XMLStreamException {
        writer.writeStartDocument(version);

    }

    /**
     * Write the XML Declaration.  Note that the encoding parameter does
     * not set the actual encoding of the underlying output.  That must
     * be set when the instance of the XMLStreamWriter is created using the
     * XMLOutputFactory
     *
     * @param encoding encoding of the xml declaration
     * @param version  version of the xml document
     * @throws XMLStreamException If given encoding does not match encoding
     *                            of the underlying stream
     */
    @Override
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        writer.writeStartDocument(encoding, version);
    }

    /**
     * Write text to the output
     *
     * @param text the value to write
     * @throws XMLStreamException
     */
    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        if (!noNewLine.contains(tagStack.peek())) {
            writer.writeCharacters("\n");
            for (int i = 0; i < level; i++) writer.writeCharacters("\t");
        }
        writer.writeCharacters(text);
    }

    /**
     * Write text to the output
     *
     * @param text  the value to write
     * @param start the starting position in the array
     * @param len   the number of characters to write
     * @throws XMLStreamException
     */
    @Override
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        if (!noNewLine.contains(tagStack.peek())) {
            writer.writeCharacters("\n");
            for (int i = 0; i < level; i++) writer.writeCharacters("\t");
        }
        writer.writeCharacters(text, start, len);
    }

    /**
     * Gets the prefix the uri is bound to
     *
     * @return the prefix or null
     * @throws XMLStreamException
     */
    @Override
    public String getPrefix(String uri) throws XMLStreamException {
        return writer.getPrefix(uri);
    }

    /**
     * Sets the prefix the uri is bound to.  This prefix is bound
     * in the scope of the current START_ELEMENT / END_ELEMENT pair.
     * If this method is called before a START_ELEMENT has been written
     * the prefix is bound in the root scope.
     *
     * @param prefix the prefix to bind to the uri, may not be null
     * @param uri    the uri to bind to the prefix, may be null
     * @throws XMLStreamException
     */
    @Override
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        writer.setPrefix(prefix, uri);
    }

    /**
     * Binds a URI to the default namespace
     * This URI is bound
     * in the scope of the current START_ELEMENT / END_ELEMENT pair.
     * If this method is called before a START_ELEMENT has been written
     * the uri is bound in the root scope.
     *
     * @param uri the uri to bind to the default namespace, may be null
     * @throws XMLStreamException
     */
    @Override
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        writer.setDefaultNamespace(uri);
    }

    /**
     * Returns the current namespace context.
     *
     * @return the current NamespaceContext
     */
    @Override
    public NamespaceContext getNamespaceContext() {
        return writer.getNamespaceContext();
    }

    /**
     * Sets the current namespace context for prefix and uri bindings.
     * This context becomes the root namespace context for writing and
     * will replace the current root namespace context.  Subsequent calls
     * to setPrefix and setDefaultNamespace will bind namespaces using
     * the context passed to the method as the root context for resolving
     * namespaces.  This method may only be called once at the start of
     * the document.  It does not cause the namespaces to be declared.
     * If a namespace URI to prefix mapping is found in the namespace
     * context it is treated as declared and the prefix may be used
     * by the StreamWriter.
     *
     * @param context the namespace context to use for this writer, may not be null
     * @throws XMLStreamException
     */
    @Override
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        writer.setNamespaceContext(context);
    }

    /**
     * Get the value of a feature/property from the underlying implementation
     *
     * @param name The name of the property, may not be null
     * @return The value of the property
     * @throws IllegalArgumentException if the property is not supported
     * @throws NullPointerException     if the name is null
     */
    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return writer.getProperty(name);
    }
}