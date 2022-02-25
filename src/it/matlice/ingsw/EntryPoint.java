package it.matlice.ingsw;

import it.matlice.ingsw.auth.password.PasswordAuthData;
import it.matlice.ingsw.data.ConfiguratorUser;
import it.matlice.ingsw.data.UserFactory;
import it.matlice.ingsw.data.impl.sqlite.CategoryFactoryImpl;
import it.matlice.ingsw.data.impl.sqlite.SQLiteConnection;
import it.matlice.ingsw.data.impl.sqlite.UserFactoryImpl;

public class EntryPoint {
    public static void main(String[] args) throws Exception {
        System.out.println("lol");

        SQLiteConnection.startInstance("jdbc:sqlite:db.sqlite");
        UserFactory uf = new UserFactoryImpl();

//        f.createUser("stefano", "stefano", UserTypes.CONFIGURATOR);
        var u = uf.getUser("stefano");
        System.out.println(u instanceof ConfiguratorUser);
        System.out.println(u.authenticate(u.getAuthMethods()[0], new PasswordAuthData("stefano")));

        var cf = new CategoryFactoryImpl();

//        var a = cf.createCategory("root", null, false);
//        var b = cf.createCategory("f1a", a, false);
//        var c = cf.createCategory("f1b", a, false);
//        var d = cf.createCategory("f2", b, false);
//        var e = cf.createCategory("f3a", c, true);
//        var f = cf.createCategory("f3b", c, true);

        var g = cf.getCategory(1);

//        var hf = new HierarchyFactoryImpl();
//        hf.createHierarchy(g);

        var h = hf.getHierarchies();

        SQLiteConnection.getInstance().getConnectionSource().close();

    }
}
