package it.matlice.ingsw;

import it.matlice.ingsw.auth.password.PasswordAuthData;
import it.matlice.ingsw.data.ConfiguratorUser;
import it.matlice.ingsw.data.UserFactory;
import it.matlice.ingsw.data.impl.jdbc.CategoryFactoryImpl;
import it.matlice.ingsw.data.impl.jdbc.HierarchyFactoryImpl;
import it.matlice.ingsw.data.impl.jdbc.JdbcConnection;
import it.matlice.ingsw.data.impl.jdbc.UserFactoryImpl;

public class EntryPoint {
    public static void main(String[] args) throws Exception {
        System.out.println("lol");

        JdbcConnection.startInstance("jdbc:sqlite:db.sqlite");
        UserFactory uf = new UserFactoryImpl();

//        uf.createUser("stefano", UserTypes.CONFIGURATOR);
        var u = uf.getUser("stefano");
        System.out.println(u instanceof ConfiguratorUser);
        System.out.println(u.getAuthMethods().get(0).performAuthentication(new PasswordAuthData("stefano")));

        var cf = new CategoryFactoryImpl();

//        var a = cf.createCategory("root", null, false);
//        a.put("campo1", new TypeDefinition<>("campo1", TypeDefinition.TypeAssociation.INTEGER, true));
//        a.put("campo2", new TypeDefinition<>("campo2", TypeDefinition.TypeAssociation.INTEGER, false));
//        cf.saveCategory(a);
//
//        var b = cf.createCategory("f1a", a, false);
//        b.put("campo3", new TypeDefinition<>("campo3", TypeDefinition.TypeAssociation.INTEGER, true));
//        cf.saveCategory(b);
//
//        var c = cf.createCategory("f1b", a, false);
//        var d = cf.createCategory("f2", b, false);
//        var e = cf.createCategory("f3a", c, true);
//        var f = cf.createCategory("f3b", c, true);
//
//        cf.saveCategory(c);
//        cf.saveCategory(d);
//        cf.saveCategory(e);
//        cf.saveCategory(f);

        var g = cf.getCategory(1);
        var hf = new HierarchyFactoryImpl();
//        hf.createHierarchy(g);
        var h = hf.getHierarchies();

        JdbcConnection.getInstance().getConnectionSource().close();

    }
}
