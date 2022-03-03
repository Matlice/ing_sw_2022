package it.matlice.ingsw;

import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.data.impl.jdbc.CategoryFactoryImpl;
import it.matlice.ingsw.data.impl.jdbc.HierarchyFactoryImpl;
import it.matlice.ingsw.data.impl.jdbc.JdbcConnection;
import it.matlice.ingsw.data.impl.jdbc.UserFactoryImpl;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.view.stream.StreamView;

import java.util.Scanner;

public class EntryPoint {
    public static void main(String[] args) throws Exception {
        JdbcConnection.startInstance("jdbc:sqlite:db.sqlite");
        var uf = new UserFactoryImpl();
        var cf = new CategoryFactoryImpl();
        var hf = new HierarchyFactoryImpl();

        var view = new StreamView(System.out, new Scanner(System.in));
        var controller = new Controller(hf, cf, uf);
        var model = new Model(view, controller);

        if (uf.getUsers().size() == 0) {
            controller.addConfiguratorUser("admin");
        }

        while (model.mainloop()) ;
    }
}
