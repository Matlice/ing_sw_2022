package it.matlice.test.utils;

import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.data.impl.jdbc.*;
import it.matlice.ingsw.view.stream.StreamView;

import java.io.*;
import java.util.Scanner;

public class E2ETestGenerator {

    public static void main(String[] args) throws Exception {
        (new E2ETestGenerator()).generate();
    }

    public void generate() throws Exception {
        var testCaseName = new Scanner(System.in).next();
        new File("test/cases/" + testCaseName).mkdirs();
        InputToOutputStream in = new InputToOutputStream(System.in, new FileOutputStream("test/cases/" + testCaseName + "/in.txt"));
        PrintStream out = new PrintStream(new TeeOutputStream(new FileOutputStream("test/cases/" + testCaseName + "/out.txt"), System.out));

        this.run(in, out);

        in.flush();
        out.flush();
    }

    public void a(InputStream in, OutputStream out) {
        var s = new Scanner(in);
        System.out.println(s.nextLine());
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

        File db = new File("db.test.sqlite");
        if (db.exists() && !db.delete()) System.out.println("Couldn't delete db");
    }

}
