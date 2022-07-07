package it.matlice.ingsw;

import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.data.impl.jdbc.*;
import it.matlice.ingsw.view.stream.StreamView;
import it.matlice.test.utils.InputToOutputStream;
import it.matlice.test.utils.TeeOutputStream;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EndToEndTest {

    @TestFactory
    Collection<DynamicTest> e2eTestFactory() {
        var tests = new LinkedList<DynamicTest>();
        var testDir = new File("test/cases/");
        for(var testCase: Objects.requireNonNull(testDir.list((current, name) -> new File(current, name).isDirectory()))){
            var in_file = new File("test/cases/" + testCase + "/in.txt");
            var out_file = new File("test/cases/" + testCase + "/out.txt");

            if(in_file.exists() && out_file.exists())
                tests.add(DynamicTest.dynamicTest(testCase, () -> testCase(in_file, out_file)));
        }

        return tests;
    }

    public void testCase(File inFile, File outFile) {
        try {
            var in = new BufferedInputStream(new FileInputStream(inFile));

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(new TeeOutputStream(os, System.out));

            this.run(in, out);

            assertEquals(Files.readString(outFile.toPath()), os.toString());
        } catch(Exception e) {
            fail();
        }
    }

    public void run(InputStream in, PrintStream out) throws Exception {

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
        var view = new StreamView(out, new Scanner(in));
        var controller = new Controller(view, model);

        controller.run();

        connection.close();
    }

    @AfterEach
    public void after() {
        this.deleteDb();
    }

    public void deleteDb() {
        File db = new File("db.test.sqlite");
        if (db.exists() && !db.delete()) System.out.println("Couldn't delete db");

    }

}
