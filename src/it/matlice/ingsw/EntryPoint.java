package it.matlice.ingsw;

import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.NodeCategory;
import it.matlice.ingsw.model.data.impl.jdbc.*;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.RequiredFieldConstrainException;
import it.matlice.ingsw.view.stream.StreamView;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Classe EntryPoint dell'applicazione, contiene il metodo main()
 */
public class EntryPoint {

    /**
     * Metodo main() dell'applicazione
     * @param args nessuno
     */
    public static void main(String[] args) throws SQLException, InvalidUserException, RequiredFieldConstrainException {
//        JdbcConnection.startInstance("jdbc:sqlite:db.sqlite");
//
//        var u = new UserFactoryImpl();
//        var c = new CategoryFactoryImpl();
//        var f = new ArticleFactoryImpl();
//
//        var cat = ((NodeCategory) c.getCategory(1)).getChildren()[0];
//        var usr = u.getUser("admin");
//
//        var map = new HashMap<String, Object>();
//        map.put("Stato di conservazione", "caldo");
//        map.put("coso", "si");
//        map.put("nativonativo", "forse");
//
//        f.makeArticle((LeafCategory) cat, usr, map);


        try {
            Logger.setGlobalLogLevel(Level.WARNING);
            JdbcConnection.startInstance("jdbc:sqlite:db.sqlite");

            // istanzia le factory necessarie per il database
            var uf = new UserFactoryImpl();
            var cf = new CategoryFactoryImpl();
            var hf = new HierarchyFactoryImpl();
            var sf = new SettingsFactoryImpl();
            var af = new ArticleFactoryImpl();

            var model = new Model(hf, cf, uf, sf, af);

            // istanzia una view sugli stream stdin e stdout
            var view = new StreamView(System.out, new Scanner(System.in));
            var controller = new Controller(view, model);

            // se non ci sono utenti creati, crea il primo con credenziali di default
            if (uf.getUsers().size() == 0) {
                controller.addDefaultConfigurator();
            }

            // esecuzione in loop del programma
            while (controller.mainloop()) ;

        } catch (SQLException e) {
            // errore durante la connessione al database, interrompe l'esecuzione
            System.out.println("Errore durante la connessione al database");
            System.exit(1);
        }

    }
}
