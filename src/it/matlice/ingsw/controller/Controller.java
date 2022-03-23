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

public class Controller {

    private final View view;
    private final Model model;
    private Authentication currentUser;

    // azioni disponibili del menu principale dell'applicazione
    private final List<MenuAction<Boolean>> user_actions = Arrays.asList(
            // "Esci" è ultimo nell'elenco ma con numero di azione zero
            new MenuAction<>("Esci", User.class, () -> false, false, 0, -1),
            new MenuAction<>("Aggiungi nuova gerarchia", ConfiguratorUser.class, this::createHierarchy),
            new MenuAction<>("Mostra gerarchie", ConfiguratorUser.class, this::showHierarchies),
            new MenuAction<>("Aggiungi nuovo configuratore", ConfiguratorUser.class, this::createConfigurator),
            new MenuAction<>("Cambia password", User.class, this::changePassword)
    );

    // azioni disponibili inizialmente ad un utente non loggato
    private final List<MenuAction<Boolean>> public_actions = Arrays.asList(
            // "Esci" è ultimo nell'elenco ma con numero di azione zero
            new MenuAction<>("Esci", User.class, () -> false, false, 0, -1),
            new MenuAction<>("Login", User.class, this::performLogin)
    );

    /**
     * Costruttore per Controller
     * @param view la view per l'interazione utente
     * @param model il model a cui richiedere i dati
     */
    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        model.setController(this);
    }

    /**
     * Mainloop dell'applicazione
     * @return false se l'esecuzione deve essere interrotta
     */
    public boolean mainloop() {
        if (this.currentUser == null)
            return this.chooseAndRun(this.public_actions, "Scegliere un'opzione");

        return this.chooseAndRun(
                this.user_actions.stream().filter(e -> e.isPermitted(this.currentUser.getUser())).toList(),
                String.format("Benvenuto %s. Scegli un'opzione", this.currentUser.getUser().getUsername())
        );
    }

    //ACTIONS===============================================================

    /**
     * Permette all'utente di effettuare il login
     * @return true
     */
    private boolean performLogin() {
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

    /**
     * Permette all'utente di cambiare la password
     * @return true
     */
    private boolean changePassword() {
        boolean passwordChanged = false;
        while (!passwordChanged) {
            try {
                var psw1 = this.view.getPassword();
                var psw2 = this.view.getPassword("Ripeti password");
                if(!psw1.equals(psw2))
                    this.view.error("Le due password inserite non coincidono");
                else{
                    this.model.changePassword(this.currentUser, psw1);
                    passwordChanged = true;
                }
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

    /**
     * Permette l'aggiunta di un utente configuratore di default al primo avvio
     */
    public void addDefaultConfigurator() {
        try {
            String psw = this.model.addConfiguratorUser("admin", true);
            this.view.info(String.format("Per il primo accesso le credenziali sono admin:%s", psw));
        } catch (DuplicateUserException | InvalidUserTypeException | InvalidPasswordException | SQLException e) {
            e.printStackTrace(); //todo
        }
    }

    /**
     * Permette all'utente di creare un nuovo utente configuratore
     * @return true
     */
    private boolean createConfigurator() {
        var username = this.view.get("Nome utente");
        try {
            String password = this.model.addConfiguratorUser(username, false);
            this.view.info("Usa " + username + ":" + password + " per il primo login");
        } catch (DuplicateUserException e) {
            this.view.error("Utente già esistente");
        } catch (InvalidUserTypeException | InvalidPasswordException | SQLException e) {
            this.view.error("Impossibile creare un nuovo configuratore");
        }
        return true;
    }

    /**
     * Permette all'utente di creare una nuova gerarchia
     * Crea la categoria radice, e tutte le sotto-categorie
     * Permette di terminare l'inserimento solo se ogni categoria non foglia ha almeno due categorie figlie
     *
     * @return true
     */
    private boolean createHierarchy() {
        // creazione della categoria root
        Category root;
        try {
            root = this.createCategory(null);
            this.makeFields(root);
        } catch (DuplicateCategoryException e) {
            this.view.error("Categoria radice con lo stesso nome già presente");
            return true;
        }

        // aggiunge i campi default alla categoria radice
        root.put("Stato di conservazione", new TypeDefinition<>(true));
        root.put("Descrizione libera", new TypeDefinition<>(false));

        // creazione delle categorie figlie
        while (this.chooseAndRun(Arrays.asList(
                new MenuAction<>("Salva ed esci", ConfiguratorUser.class, () -> false, !root.isCategoryValid(), 0, -1),
                new MenuAction<>("Aggiungi nuova categoria", ConfiguratorUser.class, () -> true)
        ), "Si vuole aggiungere una nuova categoria?")) {
            Category father = this.chooseAndRun(this.getCategorySelectionMenu(root), "Selezionare una categoria");
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

    /**
     * Permette all'utente di visualizzare le gerarchie a sistema
     * @return true
     */
    private boolean showHierarchies() {
        // mostra la lista di gerarchie disponibili
        List<Hierarchy> hierarchies = this.model.getHierarchies();
        if(hierarchies.size() == 0){
            this.view.info("Nessuna gerarchia torvata");
            return true;
        }

        // scelta della gerarchia da visualizzare
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

    //INTERNAL ACTIONS==============================================================

    /**
     * Permette di richiedere all'utente un'azione tramite un menu ed eseguire l'azione associata
     * @param actions lista di azioni disponibili
     * @param prompt messaggio da comunicare all'utente
     * @return booleano di ritorno dall'azione eseguita
     */
    private <T> T chooseAndRun(List<MenuAction<T>> actions, String prompt) {
        while (true) {
            var action = this.view.chooseOption(actions, prompt);
            if (action != null)
                return action.getAction().run();
            else {
                this.view.error("Azione non permessa");
            }
        }
    }

    /**
     * Permette all'utente di effettuare il login
     * @return true se l'utente si è autenticato
     */
    private boolean login() {
        var username = this.view.get("Utente");
        try {
            this.currentUser = this.model.authenticate(username);
            if (this.currentUser == null) {
                this.view.error("Password errata");
            }
            return this.currentUser != null;
        } catch (InvalidUserException e) {
            this.view.error("Utente non valido");
        }
        return false;
    }

    /**
     * Permette la scelta riguardo l'aggiunta di campi alla categoria parametro
     * @param c categoria a cui aggiungere i campi
     */
    private void makeFields(Category c){
        while (this.chooseAndRun(Arrays.asList(
                new MenuAction<>("No, torna all'inserimento categorie", ConfiguratorUser.class, () -> false, false, 0, -1),
                new MenuAction<>("Sì, aggiungi campo nativo", ConfiguratorUser.class, () -> this.addField(c))
        ), "Si vuole aggiungere un campo nativo?"));
    }

    /**
     * Permette la creazione di un nuovo campo in una categoria
     * @param c categoria a cui aggiungere un campo
     * @return true
     */
    private boolean addField(Category c) {
        // scelta del nome del campo, non deve essere già esistente tra i padri della categoria
        String name = null;
        while (name == null) {
             name = this.view.getLine("Nome campo").trim();
             while (name.length() == 0) {
                 this.view.warn("Inserire un nome non vuoto");
                 name = this.view.getLine("Nome campo").trim();
             }
             if (c.containsKey(name)) {
                 this.view.error("Campo già esistente nella categoria");
                 name = null;
             }
        }

        // se ci sono più tipi di campo possibili permette di sceglierlo
        // per ora è possibile inserire solo stringhe, quindi salta la scelta
        var type = TypeDefinition.TypeAssociation.values()[0];

        if (TypeDefinition.TypeAssociation.values().length >= 2) {
            type = this.view.chooseOption(
                Arrays.stream(TypeDefinition.TypeAssociation.values())
                        .map(e -> new MenuAction<>(e.toString(), ConfiguratorUser.class, () -> e))
                        .toList(), "Seleziona un tipo").getAction().run();
        }

        // chiede se la compilazione del campo è obbligatoria
        var required = this.view.get("Obbligatorio [y/N]").equalsIgnoreCase("y");

        c.put(name, new TypeDefinition<>(type, required));

        return true;
    }

    /**
     * Le categorie di default sono create come LeafCategory
     *
     * @return categoria creata
     */
    private @NotNull Category createCategory(Category root) throws DuplicateCategoryException {

        String name = this.view.getLine("Nome").trim();
        while(name.length() == 0) {
            this.view.warn("Inserire un nome non vuoto");
            name = this.view.getLine("Nome").trim();
        }
        if (root==null && !this.model.isValidRootCategoryName(name))
            throw new DuplicateCategoryException();
        else if (root!=null && !root.isValidChildCategoryName(name))
            throw new DuplicateCategoryException();

        String description = this.view.getLine("Descrizione").trim();

        return this.model.createCategory(name, description, null);
    }

    /**
     * Permette di aggiungere una categoria figlia ad una padre
     *
     * @param father categoria padre
     * @param child categoria figlia da aggiungere
     * @return la nuova categoria padre
     */
    private NodeCategory appendCategory(Category father, Category child) {
        var f = father instanceof LeafCategory ? ((LeafCategory) father).convertToNode() : (NodeCategory) father;
        f.clone();
        f.addChild(child);
        return f;
        //todo this can throw errors?
    }

    /**
     * A partire da una categoria root, crea un menu che permette di scegliere una delle categorie figlie
     * Utilizzato per scegliere la categoria padre a cui aggiungere una categoria figlia
     *
     * Passo base della ricorsione
     *
     * @param root categoria radice
     * @return la lista di MenuAction delle categorie figlie
     */
    private List<MenuAction<Category>> getCategorySelectionMenu(Category root) {
        return this.getCategorySelectionMenu(root, new LinkedList<>(), "");
    }

    /**
     * A partire da una categoria root, crea un menu che permette di scegliere una delle categorie figlie
     * Utilizzato per scegliere la categoria padre a cui aggiungere una categoria figlia
     *
     * Passo ricorsivo, aggiunge le categorie figlie
     *
     * @param root categoria radice
     * @param acc lista a cui aggiungere le MenuAction
     * @param prefix prefisso da anteporre ai nomi delle categorie
     * @return la lista completa delle categorie figlie
     */
    private List<MenuAction<Category>> getCategorySelectionMenu(Category root, List<MenuAction<Category>> acc, String prefix) {
        acc.add(new MenuAction<>(prefix + root.getName(), null, () -> root));
        if (root instanceof NodeCategory)
            for (var child : ((NodeCategory) root).getChildren())
                this.getCategorySelectionMenu(child, acc, prefix + root.getName() + " > ");
        return acc;
    }

    //FROM MODEL ===============================================================

    /**
     * In base al tipo di autenticazione, permetta al model di richiedere i dati necessari all'utente tramite la view
     * @param method il metodo di autenticazione
     * @return dati relativi all'autenticazione
     */
    public AuthData getLoginData(String method) {
        if (method.equals(PasswordAuthMethod.class.getName()))
            return PasswordAuthMethod.getAuthData(this.view.getPassword());
        else {
            this.view.error("Nessun metodo di login disponibile per " + method);
            return null;
        }
    }

}
