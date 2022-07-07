package it.matlice.ingsw;

import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.data.impl.jdbc.*;
import it.matlice.ingsw.model.exceptions.DBException;
import it.matlice.ingsw.view.stream.StreamView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

public class EndToEnd {

    @Test
    public void test() throws Exception {
        var in = new FileInputStream("test/test1.in.txt");
        this.run(in, System.out);
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

    @After
    public void after() {
        this.deleteDb();
    }

    @Before
    public void before() {
        this.deleteDb();
    }

    public void deleteDb() {
        File db = new File("db.test.sqlite");
        if (!db.delete()) System.out.println("Couldn't delete db");

    }

}
