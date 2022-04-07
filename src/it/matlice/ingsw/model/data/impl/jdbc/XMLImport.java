package it.matlice.ingsw.model.data.impl.jdbc;

import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.data.factories.CategoryFactory;
import it.matlice.ingsw.model.exceptions.CannotParseDayException;
import it.matlice.ingsw.model.exceptions.CannotParseIntervalException;
import it.matlice.ingsw.model.exceptions.DuplicateCategoryException;
import it.matlice.ingsw.model.exceptions.InvalidIntervalException;
import it.matlice.ingsw.xml.XMLConversion;

import javax.xml.stream.XMLStreamException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class XMLImport {

    public final Model model;
    public XMLImport(Model model) {
        this.model = model;
    }

    public final XMLConversion dayConverter = (node, children, parent, dinasty) -> {
        try {
            return Settings.Day.fromString(node.getValue());
        } catch (CannotParseDayException e) {
            throw new XMLStreamException();
        }
    };
    public final XMLConversion intervalConverter = (node, children, parent, dinasty) -> {
        try {
            return Interval.fromString(node.getValue());
        } catch (CannotParseIntervalException | InvalidIntervalException e) {
            throw new XMLStreamException();
        }
    };
    public XMLConversion daysConverter = (node, children, parent, dinasty) -> children.values().stream().map(e -> (Settings.Day) e).collect(Collectors.toCollection(ArrayList::new));
    public XMLConversion intervalsConverter = (node, children, parent, dinasty) -> children.values().stream().map(e -> (Interval) e).collect(Collectors.toCollection(ArrayList::new));
    public XMLConversion stringArrayConverter = (node, children, parent, dinasty) -> children.values().stream().map(e -> (String) e).collect(Collectors.toCollection(ArrayList::new));
    public XMLConversion settingsConverter = (node, children, parent, dinasty) -> {
        try {
            SettingsFactoryImpl sf = new SettingsFactoryImpl();
            return sf.makeSettings(
                    (String) children.get("city"),
                    (Integer) children.get("due"),
                    (ArrayList<String>) children.get("locations"),
                    (ArrayList<Settings.Day>) children.get("days"),
                    (ArrayList<Interval>) children.get("intervals"));
        } catch (SQLException e) {
            throw new XMLStreamException();
        }
    };

    //-------------------------------------------------------------------

    public final XMLConversion fieldConverter = (node, children, parent, dinasty) -> {
        return new AbstractMap.SimpleEntry<String, Boolean> (node.getValue(), "true".equalsIgnoreCase(node.get_attribute("required")));
    };
    public final XMLConversion fieldsConverter = (node, children, parent, dinasty) -> {
        HashMap<String, Boolean> map = new HashMap<>();
        for (var entry: children.values()) {
            Map.Entry<String, Boolean> e = (Map.Entry) entry;
            map.put(e.getKey(), e.getValue());
        }
        return  map;
    };

    public XMLConversion categoryConverter(){
        return (node, children, parent, dinasty) -> {
            try {
                var c = this.model.createCategory(
                        (String) children.get("name"),
                        (String) children.get("description"),
                        null
                );
                HashMap<String, Boolean> fields = (HashMap<String, Boolean>) children.get("fields");
                fields.forEach((k, v) -> {
                    c.put(k, new TypeDefinition(TypeDefinition.TypeAssociation.values()[0], v));
                });
                //TODO controlli sul nome delle categorie
                var childrenCategories = (ArrayList<Category>) children.get("children");
                for(var e : childrenCategories){
                    NodeCategory r = null;
                    while (r == null) {
                        r = c instanceof LeafCategory ? ((LeafCategory) c).convertToNode() : (NodeCategory) c;
                        r.addChild(e);
                    }
                }
                return c;
            } catch (Exception e) {
                throw new XMLStreamException();
            }
        };
    }
    public XMLConversion childrenConverter = (node, children, parent, dinasty) -> children.values().stream().map(e -> (Category) e).collect(Collectors.toCollection(ArrayList::new));
    public XMLConversion hierarchyConverter(){ return this.categoryConverter();}
    public XMLConversion hierarchiesConverter = (node, children, parent, dinasty) -> children.values().stream().map(e -> (Hierarchy) e).collect(Collectors.toCollection(ArrayList::new));
    
    //---------------------------------------------------------------------
    
    public XMLConversion configurationConverter = (node, children, parent, dinasty) -> new ConfigurationXML(
        (Settings) children.get("settings"),
        (ArrayList<Hierarchy>) children.get("hierarchies")
    );
    
    public class ConfigurationXML{
        public Settings settings;
        public List<Hierarchy> hierarchies;

        public ConfigurationXML(Settings settings, List<Hierarchy> hierarchies) {
            this.settings = settings;
            this.hierarchies = hierarchies;
        }
    }
    
}
