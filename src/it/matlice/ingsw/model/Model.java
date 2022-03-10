package it.matlice.ingsw.model;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.controller.Authentication;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.controller.exceptions.InvalidUserException;
import it.matlice.ingsw.controller.exceptions.LoginInvalidException;
import it.matlice.ingsw.data.*;
import it.matlice.ingsw.view.View;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Model {

    private final View view;
    private final Controller controller;
    private Authentication currentUser;

    private final List<MenuAction<Boolean>> user_actions = Arrays.asList(
            new MenuAction<>("Change password", User.class, this::changePassword),
            //new MenuAction("New Configurator", ConfiguratorUser.class, () -> true),
            new MenuAction<>("New Hierarchy", ConfiguratorUser.class, this::createHierarchy),
            //new MenuAction("Show Hierarchy", ConfiguratorUser.class, () -> true),
            new MenuAction<>("Exit", User.class, () -> false)
    );

    private final List<MenuAction<Boolean>> public_actions = Arrays.asList(
            new MenuAction<>("Login", User.class, this::performLogin),
            new MenuAction<>("Exit", User.class, () -> false)
    );

    public Model(View view, Controller c) {
        this.view = view;
        this.controller = c;
        c.setModel(this);
    }

    private boolean chooseAndRun(List<MenuAction<Boolean>> actions, String prompt) {
        var action = this.view.choose(actions, prompt, null);
        if (action != null)
            return action.getAction().run();
        else {
            this.view.message("ERRORE", "Cannot retrive action informations");
            return true;
        }
    }

    public boolean mainloop() {
        if (this.currentUser == null)
            return this.chooseAndRun(this.public_actions, "Sciegliere un'opzione");

        return this.chooseAndRun(this.user_actions.stream().filter(e -> e.isPermitted(this.currentUser.getUser())).toList(), String.format("Benvenuto %s. Sciegli un'opzione", this.currentUser.getUser().getUsername()));
    }

    //ACTIONS===============================================================

    public boolean performLogin() {
        if (this.login()) {
            if (this.currentUser.getUser().getLastLoginTime() == null) {
                this.view.message("WARN", "You need to change your password");
                this.changePassword();
            }
            this.controller.finalizeLogin(this.currentUser);
            return true;
        }
        return true;
    }

    public boolean changePassword() {
        try {
            var psw = this.view.changePassword();
            this.controller.changePassword(this.currentUser, psw);
            return true;
        } catch (InvalidPasswordException e) {
            this.view.message("Errore", "La password non rispetta i requisiti di sicurezza");
        } catch (LoginInvalidException e) {
            this.view.message("Errore", "Il login non è più valido.");
        } catch (Exception e) {
            this.view.message("Errore", e.getMessage());
        }
        return true;
    }

    public boolean createHierarchy() {
        var root = this.createCategory(); //todo add descrizione to categoria

        while (this.view.choose(Arrays.asList(
                new MenuAction<>("Aggiunti nuova categoria", ConfiguratorUser.class, () -> true),
                new MenuAction<>("Conferma ed esci", ConfiguratorUser.class, () -> false, !this.controller.isCategoryValid(root))
        ), "Si vuole aggiungere una nuova categoria?", true).getAction().run()) {
            var father = this.view.choose(this.getCategories(root), "Selezionare una categoria", null).getAction().run();
            if (father == null) continue;

            var r = this.appendCategory(father, this.createCategory());
            if (father == root)
                root = r;
        }

        this.controller.createHierarchy(root);
        return true;
    }

    //FROM CONTROLLER===============================================================

    public AuthData getLoginData(String method) {
        if (method.equals(PasswordAuthMethod.class.getName()))
            return PasswordAuthMethod.getAuthData(this.view.getPassword());
        else {
            this.view.message("Errore", "Nessun metodo di login disponibile per " + method);
            return null;
        }
    }

    //INTERNAL ACTIONS==============================================================

    public boolean login() {
        var username = this.view.getLoginUsername();
        try {
            this.currentUser = this.controller.authenticate(username);
            if (this.currentUser == null) {
                this.view.message("Errore", "password errata");
            }
            return this.currentUser != null;
        } catch (InvalidUserException e) {
            this.view.message("Errore", "Invalid user");
        }
        return false;
    }

    public void addField(Category c) {
        var name = this.view.get("Nome campo");
        var type = this.view.choose(
                Arrays.stream(TypeDefinition.TypeAssociation.values())
                        .map(e -> new MenuAction<>(e.toString(), ConfiguratorUser.class, () -> e))
                        .toList(), "Seleziona un tipo", TypeDefinition.TypeAssociation.STRING).getAction().run();
        var required = this.view.get("Obbligatorio [y/N]").equalsIgnoreCase("y");
        c.put(name, new TypeDefinition<>(type, required));
    }

    /**
     * Le categorie di default sono create come LeafCategory
     *
     * @return
     */
    public Category createCategory() {
        var category = this.controller.createCategory(this.view.get("Category name"), null);
        while (this.view.choose(Arrays.asList(
                new MenuAction<>("Aggiunti campo nativo", ConfiguratorUser.class, () -> true),
                new MenuAction<>("Conferma", ConfiguratorUser.class, () -> false)
        ), "Si vuole aggiungere un campo nativo?", true).getAction().run()) {
            this.addField(category);
        }
        return category;
    }

    public NodeCategory appendCategory(Category father, Category child) {
        var f = father instanceof LeafCategory ? ((LeafCategory) father).convertToNode() : (NodeCategory) father;
        f.addChild(child);
        return f;
        //todo this can throw errors?
    }

    private List<MenuAction<Category>> getCategories(Category root) {
        return this.getCategories(root, new LinkedList<>(), "");
    }

    private List<MenuAction<Category>> getCategories(Category root, List<MenuAction<Category>> acc, String prefix) {
        acc.add(new MenuAction<>(prefix + root.getName(), null, () -> root));
        if (root instanceof NodeCategory)
            for (var child : ((NodeCategory) root).getChildren())
                this.getCategories(child, acc, prefix + root.getName() + ".");
        return acc;
    }
}
