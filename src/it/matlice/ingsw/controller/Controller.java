package it.matlice.ingsw.controller;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.model.Authentication;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.LoginInvalidException;
import it.matlice.ingsw.data.*;
import it.matlice.ingsw.view.View;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Controller {

    private final View view;
    private final Model model;
    private Authentication currentUser;

    private final List<MenuAction<Boolean>> user_actions = Arrays.asList(
            new MenuAction<>("Cambia Password", User.class, this::changePassword),
            new MenuAction<>("Aggiungi nuovo Configuratore", ConfiguratorUser.class, this::createConfigurator),
            new MenuAction<>("Aggiungi nuova Gerarchia", ConfiguratorUser.class, this::createHierarchy),
            //new MenuAction("Show Hierarchy", ConfiguratorUser.class, () -> true),
            new MenuAction<>("Esci", User.class, () -> false)
    );

    private final List<MenuAction<Boolean>> public_actions = Arrays.asList(
            new MenuAction<>("Login", User.class, this::performLogin),
            new MenuAction<>("Esci", User.class, () -> false)
    );

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        model.setController(this);
    }

    private boolean chooseAndRun(List<MenuAction<Boolean>> actions, String prompt) {
        var action = this.view.chooseOption(actions, prompt, null);
        if (action != null)
            return action.getAction().run();
        else {
            this.view.message("ERRORE", "Azione non permessa");
            return true;
        }
    }

    public boolean mainloop() {
        if (this.currentUser == null)
            return this.chooseAndRun(this.public_actions, "Scegliere un'opzione");

        return this.chooseAndRun(this.user_actions.stream().filter(e -> e.isPermitted(this.currentUser.getUser())).toList(), String.format("Benvenuto %s. Scegli un'opzione", this.currentUser.getUser().getUsername()));
    }

    //ACTIONS===============================================================

    public boolean performLogin() {
        if (this.login()) {
            if (this.currentUser.getUser().getLastLoginTime() == null) {
                this.view.message("WARN", "Cambia le credenziali di accesso");
                this.changePassword();
            }
            try {
                this.model.finalizeLogin(this.currentUser);
            } catch (Exception e) {
                e.printStackTrace(); //todo
            }
            return true;
        }
        return true;
    }

    public boolean changePassword() {
        boolean passwordChanged = false;
        while (!passwordChanged) {
            try {
                var psw = this.view.changePassword();
                if(!psw[0].equals(psw[1])) this.view.message("Errore", "Le due password inserite non coincidono");
                else{
                    this.model.changePassword(this.currentUser, psw[0]);
                    passwordChanged = true;
                }
            } catch (InvalidPasswordException e) {
                this.view.message("Errore", "La password non rispetta i requisiti di sicurezza");
            } catch (LoginInvalidException e) {
                this.view.message("Errore", "Il login non è più valido.");
            } catch (Exception e) {
                this.view.message("Errore", e.getMessage());
            }
        }
        return true;
    }

    public boolean createConfigurator() {
        var username = this.view.getNewConfiguratorUsername();
        try {
            String password = this.model.addConfiguratorUser(username);
            this.view.showNewConfiguratorUserAndPassword(username, password);
        } catch (Exception e) {
            this.view.message("Errore", "Impossibile creare un nuovo configuratore");
        }
        return true;
    }

    public boolean createHierarchy() {
        Category root = this.createCategory(); //todo add descrizione to categoria

        while (this.view.chooseOption(Arrays.asList(
                new MenuAction<>("Aggiungi nuova categoria", ConfiguratorUser.class, () -> true),
                new MenuAction<>("Conferma ed esci", ConfiguratorUser.class, () -> false, !this.model.isCategoryValid(root))
        ), "Si vuole aggiungere una nuova categoria?", true).getAction().run()) {
            Category father = this.view.chooseOption(this.getCategories(root), "Selezionare una categoria", null).getAction().run();
            if (father == null) continue;

            NodeCategory r = this.appendCategory(father, this.createCategory());
            if (father == root) root = r;
        }

        try {
            this.model.createHierarchy(root);
        } catch (Exception e) {
            e.printStackTrace(); //todo
        }
        return true;
    }

    //FROM MODEL ===============================================================

    public AuthData getLoginData(String method) {
        if (method.equals(PasswordAuthMethod.class.getName()))
            return PasswordAuthMethod.getAuthData(this.view.getPassword());
        else {
            this.view.message("Errore", "Nessun metodo di login disponibile per " + method);
            return null;
        }
    } //todo, appunto di rob: teto, dobbiamo fare un discorsetto...

    //INTERNAL ACTIONS==============================================================

    public boolean login() {
        var username = this.view.getLoginUsername();
        try {
            this.currentUser = this.model.authenticate(username);
            if (this.currentUser == null) {
                this.view.message("Errore", "password errata");
            }
            return this.currentUser != null;
        } catch (InvalidUserException e) {
            this.view.message("Errore", "Utente non valido");
        }
        return false;
    }

    public void addField(Category c) {
        var name = this.view.get("Nome campo");
        var type = this.view.chooseOption(
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
        Category category = null;
        try {
            category = this.model.createCategory(this.view.get("Category name"), null);
        } catch (Exception e) {
            e.printStackTrace(); //todo
        }
        while (this.view.chooseOption(Arrays.asList(
                new MenuAction<>("Aggiunti campo nativo", ConfiguratorUser.class, () -> true),
                new MenuAction<>("Conferma", ConfiguratorUser.class, () -> false)
        ), "Si vuole aggiungere un campo nativo?", true).getAction().run()) {
            this.addField(category);
        }
        return category;
    }

    private NodeCategory appendCategory(Category father, Category child) {
        var f = father instanceof LeafCategory ? ((LeafCategory) father).convertToNode() : (NodeCategory) father;
        f.addChild(child);
        return f;
        //todo this can throw errors?
    }

    //todo ask teto, ha senso che il controller abbia dei getter?

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

    public void addDefaultConfigurator() {
        try {
            String psw = this.model.addConfiguratorUser("admin");
            this.view.message("INFO", String.format("Login con admin:%s", psw));
        } catch (Exception e) {
            e.printStackTrace(); //todo
        }

    }
}
