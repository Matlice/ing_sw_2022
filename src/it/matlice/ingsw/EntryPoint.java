package it.matlice.ingsw;

import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.data.impl.jdbc.CategoryFactoryImpl;
import it.matlice.ingsw.data.impl.jdbc.HierarchyFactoryImpl;
import it.matlice.ingsw.data.impl.jdbc.JdbcConnection;
import it.matlice.ingsw.data.impl.jdbc.UserFactoryImpl;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.view.stream.StreamView;

import java.util.Scanner;

public class EntryPoint {
    public static void main(String[] args) throws Exception {
        Logger.setGlobalLogLevel(Level.WARNING);
        JdbcConnection.startInstance("jdbc:sqlite:db.sqlite");

        var uf = new UserFactoryImpl();
        var cf = new CategoryFactoryImpl();
        var hf = new HierarchyFactoryImpl();

        var model = new Model(hf, cf, uf);
        var view = new StreamView(System.out, new Scanner(System.in));
        var controller = new Controller(view, model);

        if (uf.getUsers().size() == 0) {
            controller.addDefaultConfigurator();
        }

        while(controller.mainloop());
    }
}
