package it.matlice.ingsw.model.data.impl.jdbc;

import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.exceptions.CannotParseDayException;
import it.matlice.ingsw.model.exceptions.CannotParseIntervalException;
import it.matlice.ingsw.model.exceptions.InvalidIntervalException;
import it.matlice.ingsw.xml.Utils;
import it.matlice.ingsw.xml.XMLConversion;
import it.matlice.ingsw.xml.XMLParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class XMLImport {

    private final FileInputStream file;
    public XMLImport(FileInputStream file) {
        this.file = file;
    }

    private static final XMLConversion DayConverter = (node, children, parent, dinasty) -> {
        try {
            return Settings.Day.fromString(node.getValue());
        } catch (CannotParseDayException e) {
            throw new XMLStreamException();
        }
    };

    private static final XMLConversion DaysConverter = (node, children, parent, dinasty) -> children.values()
            .stream()
            .map(e -> (Settings.Day) e)
            .collect(Collectors.toCollection(ArrayList::new));

    private static final XMLConversion IntervalConverter = (node, children, parent, dinasty) -> {
        try {
            return Interval.fromString(node.getValue());
        } catch (CannotParseIntervalException | InvalidIntervalException e) {
            throw new XMLStreamException();
        }
    };

    private static final XMLConversion IntervalsConverter = (node, children, parent, dinasty) -> children.values()
            .stream()
            .map(e -> (Interval) e)
            .collect(Collectors.toCollection(ArrayList::new));

    private static final XMLConversion StringArrayConverter = (node, children, parent, dinasty) -> children.values()
            .stream()
            .map(e -> (String) e)
            .collect(Collectors.toCollection(ArrayList::new));

    private static final XMLConversion FieldConverter = (node, children, parent, dinasty) -> new FieldXML(node.getValue(),
            "true".equalsIgnoreCase(node.get_attribute("required")));

    private static final XMLConversion FieldsConverter = (node, children, parent, dinasty) -> children.values()
            .stream()
            .map(e -> (FieldXML) e)
            .collect(Collectors.toCollection(ArrayList::new));

    private static final XMLConversion CategoryConverter = (node, children, parent, dinasty) -> new CategoryXML(
            (String) children.get("name"),
            (String) children.get("description"),
            (List<FieldXML>) children.get("fields"),
            (List<CategoryXML>) children.get("children"));

    private static final XMLConversion ChildrenConverter = (node, children, parent, dinasty) -> children.values()
            .stream()
            .map(e -> (CategoryXML) e)
            .collect(Collectors.toCollection(ArrayList::new));

    private static final XMLConversion HierarchyConverter = (node, children, parent, dinasty) -> new HierarchyXML(
            new CategoryXML((String) children.get("name"),
                    (String) children.get("description"),
                    (List<FieldXML>) children.get("fields"),
                    (List<CategoryXML>) children.get("children")));

    private static final XMLConversion HierarchiesConverter = (node, children, parent, dinasty) -> children.values()
            .stream()
            .map(e -> (HierarchyXML) e)
            .collect(Collectors.toCollection(ArrayList::new));

    private static final XMLConversion SettingsConverter = (node, children, parent, dinasty) -> new SettingsXML((String) children.get("city"),
            (ArrayList<String>) children.get("places"),
            (ArrayList<Settings.Day>) children.get("days"),
            (ArrayList<Interval>) children.get("intervals"),
            (Integer) children.get("expiration"));

    private static final XMLConversion ConfigurationConverter = (node, children, parent, dinasty) -> new ConfigurationXML(
            (SettingsXML) children.get("settings"),
            (List<HierarchyXML>) children.get("hierarchies"));

    public ConfigurationXML parse() throws XMLStreamException {
        XMLParser parser = new XMLParser(this.file);
        parser.add_conversion("configuration", ConfigurationConverter)
                .add_conversion("settings", SettingsConverter)
                .add_conversion("city", Utils.StringConverter)
                .add_conversion("places", StringArrayConverter)
                .add_conversion("place", Utils.StringConverter)
                .add_conversion("days", DaysConverter)
                .add_conversion("day", DayConverter)
                .add_conversion("intervals", IntervalsConverter)
                .add_conversion("interval", IntervalConverter)
                .add_conversion("expiration", Utils.IntegerConverter)
                .add_conversion("hierarchies", HierarchiesConverter)
                .add_conversion("hierarchy", HierarchyConverter)
                .add_conversion("name", Utils.StringConverter)
                .add_conversion("description", Utils.StringConverter)
                .add_conversion("fields", FieldsConverter)
                .add_conversion("field", FieldConverter)
                .add_conversion("children", ChildrenConverter)
                .add_conversion("category", CategoryConverter);
        return (ConfigurationXML) parser.parse();
    }

    // Internal classes, used to represent the parsed XML only

    public static class ConfigurationXML{
        public SettingsXML settings;
        public List<HierarchyXML> hierarchies;

        public ConfigurationXML(SettingsXML settings, List<HierarchyXML> hierarchies) {
            this.settings = settings;
            this.hierarchies = hierarchies;
        }
    }

    public static class SettingsXML {
        public String city;
        public List<String> locations;
        public List<Settings.Day> days;
        public List<Interval> intervals;
        public int expiration;

        public SettingsXML(String city, List<String> locations, List<Settings.Day> days, List<Interval> intervals, int expiration) {
            this.city = city;
            this.locations = locations;
            this.days = days;
            this.intervals = intervals;
            this.expiration = expiration;
        }
    }

    public static class CategoryXML {
        public String name;
        public String description;
        public List<FieldXML> fields;
        public List<CategoryXML> categories;

        public CategoryXML(String name, String description, List<FieldXML> fields, List<CategoryXML> categories) {
            this.name = name;
            this.description = description;
            this.fields = fields;
            this.categories = categories;
        }
    }

    public static class FieldXML {
        public String name;
        public boolean required;
        public FieldTypeXML type = FieldTypeXML.STRING;

        public FieldXML(String name, boolean required, FieldTypeXML type) {
            this.name = name;
            this.required = required;
            this.type = type;
        }

        public FieldXML(String name, boolean required) {
            this.name = name;
            this.required = required;
        }
    }

    public enum FieldTypeXML {
        STRING
    }

    public static class HierarchyXML {
        public CategoryXML root;

        public HierarchyXML(CategoryXML root) {
            this.root = root;
        }
    }
    
}
