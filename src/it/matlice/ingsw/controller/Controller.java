package it.matlice.ingsw.controller;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.model.Authentication;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.exceptions.*;
import it.matlice.ingsw.data.*;
import it.matlice.ingsw.view.View;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static it.matlice.ingsw.auth.password.PasswordAuthMethod.isPasswordValid;

public class Controller {

    private final View view;
    private final Model model;
    private Authentication currentUser;

    private final List<MenuAction<Boolean>> user_actions = Arrays.asList(
            new MenuAction<>("Logout", User.class, () -> {this.currentUser = null; return true;}, false, 0, -1),
            new MenuAction<>("Aggiungi nuova gerarchia", ConfiguratorUser.class, this::createHierarchy),
            new MenuAction<>("Mostra gerarchie", User.class, this::showHierarchies),
            new MenuAction<>("Aggiungi nuovo configuratore", ConfiguratorUser.class, this::createConfigurator),
            new MenuAction<>("Cambia password", User.class, this::changePassword)
    );

    private final List<MenuAction<Boolean>> public_actions = Arrays.asList(
            new MenuAction<>("Esci", null, () -> false, false, 0, -1),
            new MenuAction<>("Login", null, this::performLogin),
            new MenuAction<>("Registrati", null, this::registerUser)
    );

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        model.setController(this);
    }

    private boolean chooseAndRun(List<MenuAction<Boolean>> actions, String prompt) {
        var action = this.view.chooseOption(actions, prompt);
        if (action != null)
            return action.getAction().run();
        else {
            this.view.error("Azione non permessa");
            return true;
        }
    }

    public boolean mainloop() {
        if (this.currentUser == null)
            return this.chooseAndRun(this.public_actions, "Scegliere un'opzione");

        return this.chooseAndRun(
                this.user_actions.stream().filter(e -> e.isPermitted(this.currentUser.getUser())).toList(),
                String.format("Benvenuto %s. Scegli un'opzione", this.currentUser.getUser().getUsername())
        );
    }

    //ACTIONS===============================================================

    public boolean performLogin() {
        if (this.login()) {
            if (this.currentUser.getUser().getLastLoginTime() == null) {
                this.view.warn("Cambia le credenziali di accesso");
                this.changePassword();
            }
            try {
                this.model.finalizeLogin(this.currentUser);
            } catch (SQLException e) {
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
                this.model.changePassword(this.currentUser, getNewPassword());
                passwordChanged = true;
            } catch (InvalidPasswordException e) {
                this.view.error("La password non rispetta i requisiti di sicurezza");
            } catch (LoginInvalidException e) {
                this.view.error("Il login non è più valido.");
            } catch (SQLException e) {
                this.view.error(e.getMessage());
            }
        }
        return true;
    }

    public boolean createConfigurator() {
        var username = this.view.get("New configurator username");
        try {
            String password = this.model.addConfiguratorUser(username);
            this.view.info("Usa " + username + ":" + password + " per il primo login");
        } catch (DuplicateUserException e) {
            this.view.error("Utente già esistente");
        } catch (InvalidUserTypeException | InvalidPasswordException | SQLException e) {
            this.view.error("Impossibile creare un nuovo configuratore");
        }
        return true;
    }

    private void makeFields(Category category){
        while (this.view.chooseOption(Arrays.asList(
                new MenuAction<>("No, torna all'inserimento categorie", ConfiguratorUser.class, () -> false, false, 0, -1),
                new MenuAction<>("Sì, aggiungi campo nativo", ConfiguratorUser.class, () -> true)
        ), "Si vuole aggiungere un campo nativo?").getAction().run()) {
            this.addField(category);
        }
    }

    public boolean createHierarchy() {
        Category root;
        try {
            root = this.createCategory(null);
            this.makeFields(root);
        } catch (DuplicateCategoryException e) {
            this.view.error("Categoria radice con lo stesso nome già presente");
            return true;
        }

        assert root != null;

        root.put("Stato di conservazione", new TypeDefinition<>(true));
        root.put("Descrizione libera", new TypeDefinition<>(false));

        while (this.chooseAndRun(Arrays.asList(
                new MenuAction<>("Salva ed esci", ConfiguratorUser.class, () -> false, !root.isCategoryValid(), 0, -1),
                new MenuAction<>("Aggiungi nuova categoria", ConfiguratorUser.class, () -> true)
        ), "Si vuole aggiungere una nuova categoria?")) {
            Category father = this.view.chooseOption(this.getCategories(root), "Selezionare una categoria").getAction().run();
            if (father == null) continue;

            NodeCategory r = null;
            while (r == null) {
                try {
                    r = this.appendCategory(father, this.createCategory(root));
                    this.makeFields(r);
                } catch (DuplicateCategoryException e) {
                    this.view.error("Categoria già esistente nell'albero della gerarchia");
                }
            }
            if (father == root) root = r;
        }

        try {
            this.model.createHierarchy(root);
        } catch (Exception e) {
            e.printStackTrace(); //todo
        }
        return true;
    }

    public boolean showHierarchies() {
        // show root categories name

        List<Hierarchy> hierarchies = this.model.getHierarchies();
        if(hierarchies.size() == 0){
            this.view.info("Nessuna gerarchia torvata");
            return true;
        }
        this.view.chooseOption(
                hierarchies.stream()
                        .map((e) -> new MenuAction<>(e.getRootCategory().getName(), User.class, () -> {
                            this.view.info(e.getRootCategory().toString());
                            return true;
                        }))
                        .collect(Collectors.toList()),
                "Quale gerarchia si vuole visualizzare?"
        ).getAction().run();

        return true;
    }

    public boolean registerUser(){
        var username = this.view.get("Utente");
        var psw = getNewPassword();

        try {
            this.model.registerUser(username, psw);
        } catch (SQLException | InvalidUserTypeException | InvalidPasswordException e) {
            this.view.error(e.getMessage());
        } catch (DuplicateUserException e) {
            this.view.error("L'utente inserito è già esistente.");
        }

        return true;
    }

    //FROM MODEL ===============================================================

    public AuthData getLoginData(@NotNull String method) {
        if (method.equals(PasswordAuthMethod.class.getName()))
            return PasswordAuthMethod.getAuthData(this.view.getPassword());
        else {
            this.view.error("Nessun metodo di login disponibile per " + method);
            return null;
        }
    }

    //INTERNAL ACTIONS==============================================================

    private @NotNull String getNewPassword(){
        String psw;
        while(true){
            psw = this.view.getPassword("Password");
            if(!psw.equals(this.view.getPassword("Ripeti la password"))) {
                this.view.error("Le password non corrispondono");
                continue;
            }
            if(!isPasswordValid(psw)){
                this.view.error("La password non rispetta i requisiti di sicurezza");
                continue;
            }
            return psw;
        }
    }

    private boolean login() {
        var username = this.view.get("Utente");
        try {
            this.currentUser = this.model.authenticate(username);
            if (this.currentUser == null) {
                this.view.error("password errata");
            }
            return this.currentUser != null;
        } catch (InvalidUserException e) {
            this.view.error("Utente non valido");
        }
        return false;
    }

    private void addField(Category c) {
        String name = null;
        while (name == null) {
             name = this.view.getLine("Nome campo");
             if (c.containsKey(name)) {
                 this.view.error("Campo già esistente nella categoria");
                 name = null;
             }
        }

        var type = TypeDefinition.TypeAssociation.values()[0];

        // only one type possible for now, let the user choose only if needed
        if (TypeDefinition.TypeAssociation.values().length >= 2) {
            type = this.view.chooseOption(
                Arrays.stream(TypeDefinition.TypeAssociation.values())
                        .map(e -> new MenuAction<>(e.toString(), ConfiguratorUser.class, () -> e))
                        .toList(), "Seleziona un tipo").getAction().run();
        }

        var required = this.view.get("Obbligatorio [y/N]").equalsIgnoreCase("y");
        c.put(name, new TypeDefinition<>(type, required));
    }

    /**
     * Le categorie di default sono create come LeafCategory
     *
     * @return categoria creata
     */
    public Category createCategory(Category root) throws DuplicateCategoryException {

        String name = this.view.getLine("Category name");
        if (root==null && !this.model.isValidRootCategoryName(name))
            throw new DuplicateCategoryException();
        else if (root!=null && !root.isValidChildCategoryName(name))
            throw new DuplicateCategoryException();

        String description = this.view.getLine("Category description");

        return this.model.createCategory(name, description, null);
    }

    private NodeCategory appendCategory(Category father, Category child) {
        var f = father instanceof LeafCategory ? ((LeafCategory) father).convertToNode() : (NodeCategory) father;
        f.clone();
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
                this.getCategories(child, acc, prefix + root.getName() + " > ");
        return acc;
    }

    public void addDefaultConfigurator() {
        try {
            String psw = this.model.addConfiguratorUser("admin");
            this.view.info(String.format("Per il primo accesso le credenziali sono admin:%s", psw));
        } catch (Exception e) {
            e.printStackTrace(); //todo
        }

    }
}
