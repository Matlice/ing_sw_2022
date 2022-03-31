package it.matlice.ingsw;

import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import it.matlice.ingsw.model.data.impl.jdbc.*;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.view.stream.StreamView;

import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

/**
 * Classe EntryPoint dell'applicazione, contiene il metodo main()
 */
public class EntryPoint {

    /**
     * Metodo main() dell'applicazione
     * @param args nessuno
     */
    public static void main(String[] args) {
//
//        try {
//            JdbcConnection.startInstance("jdbc:sqlite:db.sqlite");
//            var uf = new UserFactoryImpl();
//            var cf = new CategoryFactoryImpl();
//            var hf = new HierarchyFactoryImpl();
//            var sf = new SettingsFactoryImpl();
//            var af = new OfferFactoryImpl(sf);
//            var mf = new MessageFactoryImpl();
//
//            var u = uf.getUser("pippo");
//            var o = af.getOffers(u).get(0);
//            var m1 = mf.send(o.getLinkedOffer(), "ciao", new Date(10));
//
//            var u2 = uf.getUser("pluto");
//            var ms = mf.getUserMessages(u2);
//            System.out.println(ms);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        System.exit(0);

        try {
            Logger.setGlobalLogLevel(Level.WARNING);
            JdbcConnection.startInstance("jdbc:sqlite:db.sqlite");

            // istanzia le factory necessarie per il database
            var uf = new UserFactoryImpl();
            var cf = new CategoryFactoryImpl();
            var hf = new HierarchyFactoryImpl();
            var sf = new SettingsFactoryImpl();
            var af = new OfferFactoryImpl(sf);
            var mf = new MessageFactoryImpl();

            var model = new Model(hf, cf, uf, sf, af, mf);

            // istanzia una view sugli stream stdin e stdout
            var view = new StreamView(System.out, new Scanner(System.in));
            var controller = new Controller(view, model);

            // se non ci sono utenti creati, crea il primo con credenziali di default
            if (uf.getUsers().size() == 0) {
                controller.addDefaultConfigurator();
            }

            // esecuzione in loop del programma
            while (controller.mainloop());

        } catch (SQLException e) {
            // errore durante la connessione al database, interrompe l'esecuzione
            System.out.println("Errore durante la connessione al database");
            System.exit(1);
        }

    }
}
