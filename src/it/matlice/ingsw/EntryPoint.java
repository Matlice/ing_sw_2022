package it.matlice.ingsw;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import it.matlice.ingsw.model.data.impl.jdbc.*;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.model.exceptions.DBException;
import it.matlice.ingsw.view.stream.StreamView;

import java.sql.SQLException;
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
        try {
            // imposta il logger al livello di WARNING per togliere scritte non volute da stdout
            Logger.setGlobalLogLevel(Level.WARNING);
            var connection = new JdbcConnection("jdbc:sqlite:db.sqlite");

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

            controller.run();

        } catch (DBException e) {
            // errore durante la connessione al database, interrompe l'esecuzione
            System.out.println("Errore durante la connessione al database");
            System.exit(1);
        }

    }
}
