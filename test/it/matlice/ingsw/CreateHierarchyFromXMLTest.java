package it.matlice.ingsw;

import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.NodeCategory;
import it.matlice.ingsw.model.data.impl.jdbc.*;
import it.matlice.ingsw.view.stream.StreamView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class CreateHierarchyFromXMLTest {
/**/
    @Test
    public void createHierarchyFromXML_empty() throws Exception {
        assertThrows(Exception.class, () -> run(getEmptyHierarchyXML()));
    }

    @Test
    public void createHierarchyFromXML_noFields() throws Exception {
        var list = run(getNoFieldsHierarchyXML());

        assertEquals(1, list.size());
        Hierarchy h = list.get(0);

        assertEquals(h.getRootCategory().getName(), "Vestiti");
        assertEquals(h.getRootCategory().getDescription(), "Descrizione di Vestiti");
        assertNull(h.getRootCategory().getFather());

        assertEquals(h.getRootCategory().size(), 2);
        assertNotNull(h.getRootCategory().get("Stato di conservazione"));
        assertEquals(h.getRootCategory().get("Stato di conservazione").required(), true);
        assertNotNull(h.getRootCategory().get("Descrizione libera"));
        assertEquals(h.getRootCategory().get("Descrizione libera").required(), false);

        assertTrue(h.getRootCategory() instanceof LeafCategory);
    }

    @Test
    public void createHierarchyFromXML_single() throws Exception {
        var list = run(getNoChildHierarchyXML());

        assertEquals(1, list.size());
        Hierarchy h = list.get(0);

        assertEquals(h.getRootCategory().getName(), "Vestiti");
        assertEquals(h.getRootCategory().getDescription(), "Descrizione di Vestiti");
        assertNull(h.getRootCategory().getFather());

        assertEquals(h.getRootCategory().size(), 4);
        assertNotNull(h.getRootCategory().get("Taglia"));
        assertEquals(h.getRootCategory().get("Taglia").required(), true);
        assertNotNull(h.getRootCategory().get("Colore"));
        assertEquals(h.getRootCategory().get("Colore").required(), false);
        assertNotNull(h.getRootCategory().get("Stato di conservazione"));
        assertEquals(h.getRootCategory().get("Stato di conservazione").required(), true);
        assertNotNull(h.getRootCategory().get("Descrizione libera"));
        assertEquals(h.getRootCategory().get("Descrizione libera").required(), false);

        assertTrue(h.getRootCategory() instanceof LeafCategory);
    }

    @Test
    public void createHierarchyFromXML_singleNode() throws Exception {
        var list = run(getSingleNodeHierarchyXML());

        assertEquals(1, list.size());
        Hierarchy h = list.get(0);

        assertEquals(h.getRootCategory().getName(), "Vestiti");
        assertEquals(h.getRootCategory().getDescription(), "Descrizione di Vestiti");
        assertNull(h.getRootCategory().getFather());

        assertEquals(h.getRootCategory().size(), 4);
        assertNotNull(h.getRootCategory().get("Taglia"));
        assertEquals(h.getRootCategory().get("Taglia").required(), true);
        assertNotNull(h.getRootCategory().get("Colore"));
        assertEquals(h.getRootCategory().get("Colore").required(), false);
        assertNotNull(h.getRootCategory().get("Stato di conservazione"));
        assertEquals(h.getRootCategory().get("Stato di conservazione").required(), true);
        assertNotNull(h.getRootCategory().get("Descrizione libera"));
        assertEquals(h.getRootCategory().get("Descrizione libera").required(), false);

        assertTrue(h.getRootCategory() instanceof NodeCategory);
        var children = ((NodeCategory) h.getRootCategory()).getChildren();

        assertEquals(2, children.length);
        assertEquals(children[0].getName(), "Pantaloni");
        assertEquals(children[0].getDescription(), "Descrizione di Pantaloni");
        assertEquals(children[1].getName(), "Felpe");
        assertEquals(children[1].getDescription(), "Descrizione di Felpe");

        assertEquals(children[0].size(), 5);
        assertEquals(children[1].size(), 4);
        assertNotNull(children[0].get("Giro Vita"));
        assertEquals(children[0].get("Giro Vita").required(), false);

        assertTrue(children[0] instanceof LeafCategory);
        assertTrue(children[1] instanceof LeafCategory);
    }

    @Test
    public void createHierarchyFromXML_example() throws Exception {
        var list = run(getExampleHierarchyXML());

        assertEquals(1, list.size());
        Hierarchy h = list.get(0);

        assertEquals(h.getRootCategory().getName(), "Vestiti");
        assertEquals(h.getRootCategory().getDescription(), "Descrizione di Vestiti");
        assertNull(h.getRootCategory().getFather());

        assertEquals(h.getRootCategory().size(), 4);
        assertNotNull(h.getRootCategory().get("Taglia"));
        assertEquals(h.getRootCategory().get("Taglia").required(), true);
        assertNotNull(h.getRootCategory().get("Colore"));
        assertEquals(h.getRootCategory().get("Colore").required(), false);
        assertNotNull(h.getRootCategory().get("Stato di conservazione"));
        assertEquals(h.getRootCategory().get("Stato di conservazione").required(), true);
        assertNotNull(h.getRootCategory().get("Descrizione libera"));
        assertEquals(h.getRootCategory().get("Descrizione libera").required(), false);

        assertTrue(h.getRootCategory() instanceof NodeCategory);
        var children = ((NodeCategory) h.getRootCategory()).getChildren();

        assertEquals(2, children.length);
        assertEquals(children[0].getName(), "Pantaloni");
        assertEquals(children[0].getDescription(), "Descrizione di Pantaloni");
        assertEquals(children[1].getName(), "Felpe");
        assertEquals(children[1].getDescription(), "Descrizione di Felpe");

        assertEquals(children[0].size(), 5);
        assertEquals(children[1].size(), 4);
        assertNotNull(children[0].get("Giro Vita"));
        assertEquals(children[0].get("Giro Vita").required(), false);

        assertTrue(children[0] instanceof NodeCategory);
        var nephews = ((NodeCategory) children[0]).getChildren();

        assertEquals(2, nephews.length);
        assertEquals(nephews[0].getName(), "Jeans");
        assertEquals(nephews[0].getDescription(), "Descrizione di Jeans");
        assertEquals(nephews[1].getName(), "Pantaloncini");
        assertEquals(nephews[1].getDescription(), "Descrizione di Pantaloncini");

        assertEquals(nephews[0].size(), 5);
        assertEquals(nephews[1].size(), 5);

        assertTrue(nephews[0] instanceof LeafCategory);
        assertTrue(nephews[1] instanceof LeafCategory);

        assertTrue(children[1] instanceof NodeCategory);
        nephews = ((NodeCategory) children[1]).getChildren();

        assertEquals(2, nephews.length);
        assertEquals(nephews[0].getName(), "Felpe senza Cappuccio");
        assertEquals(nephews[0].getDescription(), "Descrizione di senza Cappuccio");
        assertEquals(nephews[1].getName(), "Felpe con Cappuccio");
        assertEquals(nephews[1].getDescription(), "Descrizione di con Cappuccio");

        assertEquals(nephews[0].size(), 4);
        assertEquals(nephews[1].size(), 5);
        assertNotNull(nephews[1].get("Colore Cappuccio"));
        assertEquals(nephews[1].get("Colore Cappuccio").required(), true);

        assertTrue(nephews[0] instanceof LeafCategory);
        assertTrue(nephews[1] instanceof LeafCategory);
    }

    private List<Hierarchy> run(XMLImport.HierarchyXML hierarchyXml) throws Exception {
        // imposta il logger al livello di WARNING per togliere scritte non volute da stdout
        Logger.setGlobalLogLevel(Level.WARNING);
        var connection = new JdbcConnection("jdbc:sqlite:db.test.sqlite");

        // istanzia le factory necessarie per il database
        var uf = new UserFactoryImpl(connection);
        var cf = new CategoryFactoryImpl(connection);
        var hf = new HierarchyFactoryImpl(cf, connection);
        var sf = new SettingsFactoryImpl(connection);
        var of = new OfferFactoryImpl(sf, hf, uf, connection);
        var mf = new MessageFactoryImpl(of, connection);

        var model = new Model(hf, cf, uf, sf, of, mf);

        // istanzia una view sugli stream stdin e stdout
        var view = new StreamView(System.out, new Scanner(System.in));
        var controller = new Controller(view, model);

        assertEquals(0, model.getHierarchies().size());
        try{
            ReflectionUtils.invokeMethod(Controller.class.getDeclaredMethod("createHierarchyFromXML", XMLImport.HierarchyXML.class), controller, hierarchyXml);
        }
        catch (Exception e){
            connection.close();
            throw e;
        }
        var h = model.getHierarchies();
        connection.close();
        return h;
    }

    private XMLImport.HierarchyXML getEmptyHierarchyXML(){
        return new XMLImport.HierarchyXML(null);
    }

    private XMLImport.HierarchyXML getNoFieldsHierarchyXML(){
        XMLImport.CategoryXML c1 = new XMLImport.CategoryXML("Vestiti", "Descrizione di Vestiti", Arrays.asList(), Arrays.asList());

        return new XMLImport.HierarchyXML(c1);
    }

    private XMLImport.HierarchyXML getNoChildHierarchyXML(){
        XMLImport.FieldXML f1_1 = new XMLImport.FieldXML("Taglia", true);
        XMLImport.FieldXML f1_2 = new XMLImport.FieldXML("Colore", false);
        XMLImport.CategoryXML c1 = new XMLImport.CategoryXML("Vestiti", "Descrizione di Vestiti", Arrays.asList(f1_1, f1_2), Arrays.asList());

        return new XMLImport.HierarchyXML(c1);
    }

    private XMLImport.HierarchyXML getSingleNodeHierarchyXML() {
        XMLImport.CategoryXML c3 = new XMLImport.CategoryXML("Felpe", "Descrizione di Felpe", List.of(), Arrays.asList());

        XMLImport.FieldXML f2_1 = new XMLImport.FieldXML("Giro Vita", false);
        XMLImport.CategoryXML c2 = new XMLImport.CategoryXML("Pantaloni", "Descrizione di Pantaloni", List.of(f2_1), Arrays.asList());

        XMLImport.FieldXML f1_1 = new XMLImport.FieldXML("Taglia", true);
        XMLImport.FieldXML f1_2 = new XMLImport.FieldXML("Colore", false);
        XMLImport.CategoryXML c1 = new XMLImport.CategoryXML("Vestiti", "Descrizione di Vestiti", Arrays.asList(f1_1, f1_2), Arrays.asList(c2, c3));

        return new XMLImport.HierarchyXML(c1);
    }

    private XMLImport.HierarchyXML getExampleHierarchyXML(){

        XMLImport.FieldXML f8_1 = new XMLImport.FieldXML("Colore Cappuccio", true);

        XMLImport.CategoryXML c8 = new XMLImport.CategoryXML("Felpe con Cappuccio", "Descrizione di con Cappuccio", List.of(f8_1), List.of());
        XMLImport.CategoryXML c7 = new XMLImport.CategoryXML("Felpe senza Cappuccio", "Descrizione di senza Cappuccio", List.of(), List.of());

        XMLImport.CategoryXML c6 = new XMLImport.CategoryXML("Pantaloncini", "Descrizione di Pantaloncini", List.of(), List.of());
        XMLImport.CategoryXML c5 = new XMLImport.CategoryXML("Jeans", "Descrizione di Jeans", List.of(), List.of());

        XMLImport.CategoryXML c3 = new XMLImport.CategoryXML("Felpe", "Descrizione di Felpe", List.of(), Arrays.asList(c7, c8));

        XMLImport.FieldXML f2_1 = new XMLImport.FieldXML("Giro Vita", false);
        XMLImport.CategoryXML c2 = new XMLImport.CategoryXML("Pantaloni", "Descrizione di Pantaloni", List.of(f2_1), Arrays.asList(c5, c6));

        XMLImport.FieldXML f1_1 = new XMLImport.FieldXML("Taglia", true);
        XMLImport.FieldXML f1_2 = new XMLImport.FieldXML("Colore", false);
        XMLImport.CategoryXML c1 = new XMLImport.CategoryXML("Vestiti", "Descrizione di Vestiti", Arrays.asList(f1_1, f1_2), Arrays.asList(c2, c3));

        return new XMLImport.HierarchyXML(c1);
    }

    @BeforeEach
    public void before(){
        this.deleteDb();
    }

    @AfterEach
    public void after(){
        this.deleteDb();
    }

    public void deleteDb() {
        File db = new File("db.test.sqlite");
        if (db.exists() && !db.delete()) System.out.println("Couldn't delete db");
    }

}
