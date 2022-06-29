package it.matlice.ingsw.controller;

import it.matlice.ingsw.model.auth.AuthData;
import it.matlice.ingsw.model.auth.AuthMethod;
import it.matlice.ingsw.model.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.model.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.model.Authentication;
import it.matlice.ingsw.model.Model;
import it.matlice.ingsw.model.data.impl.jdbc.XMLImport;
import it.matlice.ingsw.model.exceptions.*;
import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.view.View;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static it.matlice.ingsw.controller.ErrorType.*;
import static it.matlice.ingsw.controller.WarningType.*;

public class Controller {

    private final View view;
    private final Model model;
    private Authentication currentUser = new NullAuthentication();

    // azioni disponibili del menu principale dell'applicazione
    private final List<MenuAction<Boolean>> user_actions = Arrays.asList(
            // "Esci" è ultimo nell'elenco ma con numero di azione zero
            new MenuAction<>("Logout", this::logout, false, 0, -1),
            new PrivilegedMenuAction<>("Proponi uno scambio", new User.UserTypes[]{User.UserTypes.CUSTOMER}, this::offerTrade),
            new PrivilegedMenuAction<>("Accetta uno scambio", new User.UserTypes[]{User.UserTypes.CUSTOMER}, this::acceptTrade),
            new PrivilegedMenuAction<>("Rispondi a un messaggio", new User.UserTypes[]{User.UserTypes.CUSTOMER}, this::answerMessage),
            new PrivilegedMenuAction<>("Aggiungi nuovo articolo", new User.UserTypes[]{User.UserTypes.CUSTOMER}, this::createArticle),
            new PrivilegedMenuAction<>("Ritira un'offerta aperta", new User.UserTypes[]{User.UserTypes.CUSTOMER}, this::retractOffer),
            new PrivilegedMenuAction<>("Mostra le mie offerte", new User.UserTypes[]{User.UserTypes.CUSTOMER}, this::showOffersByUser),
            new MenuAction<>("Mostra offerte per categoria", this::showOpenOffersByCategory),
            new PrivilegedMenuAction<>("Aggiungi nuova gerarchia", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, this::createHierarchy),
            new MenuAction<>("Mostra gerarchie", this::showHierarchies),
            new MenuAction<>("Mostra parametri di configurazione", this::showConfParameters),
            new PrivilegedMenuAction<>("Modifica parametri di configurazione", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, this::editConfParameters),
            new PrivilegedMenuAction<>("Importa informazioni da file testuale", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, () -> this.importConfiguration(false)),
            new PrivilegedMenuAction<>("Aggiungi nuovo configuratore", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, this::createConfigurator),

            new MenuAction<>("Cambia password", this::changePassword)
    );

    // azioni disponibili inizialmente ad un utente non loggato
    private final List<MenuAction<Boolean>> public_actions = Arrays.asList(
            // "Esci" è ultimo nell'elenco ma con numero di azione zero
            new MenuAction<>("Esci", () -> false, false, 0, -1),
            new MenuAction<>("Login", this::performLogin),
            new MenuAction<>("Registrati", this::registerUser)
    );

    /**
     * Costruttore per Controller
     *
     * @param view  la view per l'interazione utente
     * @param model il model a cui richiedere i dati
     */
    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Mainloop dell'applicazione
     *
     * @return false se l'esecuzione deve essere interrotta
     */
    public boolean mainloop() {

        if (this.model.hasConfiguredSettings())
            this.model.timeIteration();

        if (!this.currentUser.isValid())
            return this.chooseAndRun(this.public_actions, "Scegliere un'opzione");

        return this.chooseAndRun(
                this.user_actions,
                String.format("Benvenuto %s. Scegli un'opzione", this.currentUser.getUser().getUsername())
        );
    }

    //ACTIONS===============================================================

    /**
     * Effettua il logout dell'utente
     *
     * @return true
     */
    private boolean logout() {
        this.currentUser = new NullAuthentication();
        return true;
    }

    /**
     * Permette all'utente di effettuare il login
     *
     * @return true
     */
    private boolean performLogin() {
        if (this.login()) {
            if (this.currentUser.getUser().getLastLoginTime() == null) {
                this.view.warn(NEED_CHANGE_PASSWORD);
                this.changePassword();
            }
            try {
                this.finalizeLogin();

                if (this.currentUser.getUser() instanceof ConfiguratorUser)
                    while (!this.model.hasConfiguredSettings()) {
                        this.configureSettings(true);
                    }

                else {
                    if (!this.model.hasConfiguredSettings()) {
                        this.view.error(SYSTEM_NOT_CONFIGURED);
                        this.logout();
                    }
                }


            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
            return true;
        }
        return true;
    }

    private void finalizeLogin() throws SQLException {

        this.model.finalizeLogin(this.currentUser);

        if (this.currentUser.getUser() instanceof CustomerUser) {

            var selected = this.model.getSelectedOffers(this.currentUser);
            if (selected.size() > 0) {
                this.view.showList("Sei stato selezionato per degli scambi!", selected);
            }

            var messages = this.model.getUserMessages(this.currentUser);
            if (messages.size() > 0) {
                if (messages.size() == 1) {
                    this.view.showList("Hai un nuovo messaggio!", messages);
                } else {
                    this.view.showList("Hai " + messages.size() + " nuovi messaggi!", messages);
                }
            }
        }
    }

    /**
     * Permette ad un nuovo utente di registrarsi come fruitore
     *
     * @return true
     */
    public boolean registerUser() {
        var username = this.view.get("Utente");
        var psw = this.getNewPassword();

        try {
            this.model.registerUser(username, psw);
            this.view.warn(SUCCESSFUL_REGISTRATION);
        } catch (InvalidPasswordException e) {
            this.view.error(PASSWORD_NOT_VALID);
        } catch (DuplicateUserException e) {
            this.view.error(USER_DUPLICATE);
        } catch (InvalidUserTypeException | SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Permette all'utente di cambiare la password
     *
     * @return true
     */
    private boolean changePassword() {
        boolean passwordChanged = false;
        while (!passwordChanged) {
            try {
                this.model.changePassword(this.currentUser, this.getNewPassword());
                passwordChanged = true;
            } catch (InvalidPasswordException e) {
                this.view.error(PASSWORD_NOT_VALID);
            } catch (LoginInvalidException e) {
                this.view.error(USER_LOGIN_INVALID);
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
                return false;
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
            this.view.getInfoFactory().getFirstAccessCredentialsMessage("admin", psw).show();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InvalidUserTypeException | DuplicateUserException | InvalidPasswordException e) {
            this.view.error(ERR_DEFAULT_USER_CREATION);
        }
    }

    /**
     * Permette all'utente di creare un nuovo utente configuratore
     *
     * @return true
     */
    private boolean createConfigurator() {
        var username = this.view.get("Nome utente");//todo?
        try {
            String password = this.model.addConfiguratorUser(username, false);
            this.view.getInfoFactory().getFirstAccessCredentialsMessage(username, password).show();
        } catch (DuplicateUserException e) {
            this.view.error(USER_DUPLICATE);
        } catch (InvalidUserTypeException | InvalidPasswordException | SQLException e) {
            this.view.error(ERR_CONFIG_USER_CREATION);
        }
        return true;
    }

    /**
     * Permette all'utente fruitore di proporre un suo articolo
     * in scambio con un altro articolo di un altro utente appartenente alla stessa categoria
     *
     * @return true
     */
    private boolean offerTrade() {

        // scelta del proprio articolo da scambiare
        List<Offer> userOffers = this.model.getTradableOffers(this.currentUser.getUser());

        if (userOffers.size() == 0) {
            this.view.warn(NO_OFFERS_IN_EXCHANGE);
            return true;
        }
        var offerToTrade = this.view.selectItem("Quale offerta si vuole proporre in scambio?", "Esci", userOffers);
        if (offerToTrade == null) {
            return true;
        }

        // scelta dell'articolo che si vuole accettare in scambio
        List<Offer> offers = this.model.getTradableOffers(offerToTrade);
        if (offers.size() == 0) {
            this.view.warn(NO_OFFERS_IN_EXCHANGE);
            return true;
        }
        var offerToAccept = this.view.selectItem("Quale offerta si vuole accettare in scambio?", "Esci", offers);
        if (offerToAccept == null) {
            return true;
        }

        try {
            this.model.createTradeOffer(offerToTrade, offerToAccept);
            this.view.warn(SUCCESSFUL_OFFER_PROPOSAL);
        } catch (Exception e) {
            this.view.error(ERR_OFFER_PROPOSAL);
        }

        return true;
    }

    /**
     * Permette all'utente di accettare la proposta di scambio
     *
     * @return true
     */
    private boolean acceptTrade() {
        var selected_offers = this.model.getSelectedOffers(this.currentUser);

        if (selected_offers.size() == 0) {
            this.view.warn(NO_ACCEPTABLE_OFFERS);
            return true;
        }

        var offer = this.view.selectItem("Scegliere una proposta di scambio da accettare?", "Annulla", selected_offers);
        if (offer == null) {
            return true;
        }

        String place = this.chooseExchangePlace();
        Settings.Day day = this.chooseExchangeDay();
        Interval.Time time = this.chooseExchangeTime();

        var date = this.model.acceptTrade(offer, place, day, time);

        // info summary
        this.view.getInfoFactory().getProposedExchangeMessage(date, day, time).show();

        return true;
    }

    private String chooseExchangePlace() {
        // exchange place
        var places = this.model.readSettings().getLocations();
        return this.view.chooseOption(
                places.stream()
                        .map((e) -> new MenuAction<>(e, () -> e))
                        .collect(Collectors.toList()),
                "Luogo di scambio"
        ).getAction().run();
    }

    private Settings.Day chooseExchangeDay() {
        // exchange day
        this.view.getInfoFactory().getAvailableDaysMessage(this.model.readSettings().getDays()).show();
        return this.view.getLineWithConversion("Giorno di scambio", (e) -> {
            try {
                var d = Settings.Day.fromString(e);
                if (this.model.readSettings().getDays().contains(d)) {
                    return d;
                } else {
                    return null;
                }
            } catch (CannotParseDayException ex) {
                return null;
            }
        }, DAY_NOT_VALID);
    }

    private Interval.Time chooseExchangeTime() {
        // exchange time
        this.view.getInfoFactory().getAvailableIntervalsMessage(this.model.readSettings().getIntervals()).show();
        return this.view.getLineWithConversion("Ora di scambio", (e) -> {
            try {
                var t = Interval.Time.fromString(e);
                if (this.model.readSettings().getIntervals().stream().anyMatch((i) -> i.includes(t))) {
                    return t;
                } else {
                    return null;
                }
            } catch (CannotParseTimeException | InvalidTimeException ex) {
                return null;
            }
        }, HOUR_NOT_VALID);
    }

    private boolean answerMessage() {
        var messages = this.model.getUserMessages(this.currentUser);

        if (messages.size() == 0) {
            this.view.warn(NO_MESSAGES_TO_REPLY);
            return true;
        }

        var replyto = this.view.selectItem("A quale messaggio si vuol rispondere?", "Annulla", messages);
        if (replyto == null) {
            return true;
        }

        var actions = new ArrayList<MenuAction<Boolean>>();
        actions.add(0, new MenuAction<>("Annulla", () -> null, false, 0, -1));
        actions.add(new MenuAction<>("Accetta lo scambio", () -> true));
        actions.add(new MenuAction<>("Fai una controproposta", () -> false));

        var accept = this.chooseAndRun(actions, "Cosa si vuol fare?");
        if (accept == null) {
            return true;
        }

        if (accept) {
            // proposta accettata
            this.model.acceptTradeMessage(replyto);
            this.view.warn(SUCCESSFUL_ACCEPT_OFFER);
        } else {
            // proposta rifiutata, fai controproposta
            String place = this.chooseExchangePlace();
            Settings.Day day = this.chooseExchangeDay();
            Interval.Time time = this.chooseExchangeTime();

            var date = this.model.replyToMessage(replyto, place, day, time);

            // info summary
            this.view.getInfoFactory().getProposedExchangeReplyMessage(date, day, time).show();
        }

        return true;
    }

    /**
     * Permette all'utente fruitore di creare un nuovo articolo
     * appartenente ad una categoria foglia
     *
     * @return true
     */
    private boolean createArticle() {
        // scelta della categoria in cui inserire l'articolo
        if (this.model.getLeafCategories().size() == 0) {
            this.view.error(NO_LEAF_CATEGORY);
            return true;
        }
        LeafCategory cat = this.chooseLeafCategory("A quale categoria appartiene l'articolo da creare?");
        this.addArticle(cat);
        return true;
    }

    /**
     * Permette all'utente di ritirare una propria offerta aperta
     *
     * @return true
     */
    private boolean retractOffer() {
        List<Offer> offers = this.model.getRetractableOffers(this.currentUser.getUser());

        if (offers.size() == 0) {
            this.view.warn(NO_RETRACTABLE_OFFER);
            return true;
        }

        // permette all'utente di scegliere quale offerta ritirare
        var offerToRetract = this.view.selectItem("Quale offerta si vuole ritirare?", "Esci", offers);
        if (offerToRetract == null) return true;

        this.model.retractOffer(offerToRetract);
        this.view.getInfoFactory().getRetractedExchangeMessage().show();

        return true;
    }

    /**
     * Permette all'utente fruitore di visualizzare tutte
     * le proprie offerte, indipendentemente dallo stato e dalla categoria
     *
     * @return true
     */
    private boolean showOffersByUser() {
        List<Offer> offers = this.model.getOffersByUser(this.currentUser.getUser());
        this.showOffers("Le tue offerte sono le seguenti: ", offers);
        return true;
    }

    /**
     * Permette all'utente di visualizzare tutte le
     * offerte aperte relative ad una categoria foglia
     *
     * @return true
     */
    private boolean showOpenOffersByCategory() {
        if (this.model.getLeafCategories().size() == 0) {
            this.view.warn(NO_LEAF_CATEGORIES_TO_SHOW);
            return true;
        }
        LeafCategory cat = this.chooseLeafCategory("Di quale categoria si vogliono visualizzare le offerte aperte?");
        List<Offer> offers = this.model.getOffersByCategory(cat)
                .stream()
                .filter((o) -> o.getStatus() != Offer.OfferStatus.RETRACTED)
                .toList();
        this.showOffers("Le offerte aperte della categoria sono le seguenti: ", offers);
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
            this.view.error(CATEGORY_SAME_NAME_ROOT);
            return true;
        }

        // aggiunge i campi default alla categoria radice
        root.put("Stato di conservazione", new TypeDefinition(true));
        root.put("Descrizione libera", new TypeDefinition(false));

        // creazione delle categorie figlie
        while (this.chooseAndRun(Arrays.asList(
                new PrivilegedMenuAction<>("Salva ed esci", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, () -> false, !root.isCategoryValid(), 0, -1),
                new PrivilegedMenuAction<>("Aggiungi nuova categoria", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, () -> true)
        ), "Si vuole aggiungere una nuova categoria?\n(nota: una categoria non può avere una sola sottocategoria)")) {
            Category father = this.chooseAndRun(this.getCategorySelectionMenu(root), "Selezionare la categoria padre");
            if (father == null) continue;

            NodeCategory r = null;
            while (r == null) {
                try {
                    var newChild = this.createCategory(root);
                    r = this.appendCategory(father, newChild);
                    this.makeFields(newChild);
                } catch (DuplicateCategoryException e) {
                    this.view.error(CATEGORY_SAME_NAME);
                }
            }
            if (father == root) root = r;
        }

        try {
            this.model.createHierarchy(root);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return true;
    }

    /**
     * Permette all'utente di visualizzare le gerarchie a sistema
     *
     * @return true
     */
    private boolean showHierarchies() {
        // mostra la lista di gerarchie disponibili
        List<Hierarchy> hierarchies = this.model.getHierarchies();
        if (hierarchies.size() == 0) {
            this.view.warn(NO_HIERARCHIES);
            return true;
        }

        var hierarchy = this.view.selectItem("Quale gerarchia si vuole visualizzare?", null, hierarchies);
        this.view.getInfoFactory().getHierarchyInformationMessage(hierarchy).show();

        return true;
    }

    /**
     * Mostra all'utente i parametri di configurazione attuali
     *
     * @return true
     */
    private boolean showConfParameters() {
        this.view.getInfoFactory().getConfigurationMessage(this.model.readSettings()).show();
        return true;
    }

    /**
     * Permette al configuratore di modificare i parametri di configurazione,
     * ad eccezione della piazza
     *
     * @return true
     */
    private boolean editConfParameters() {
        this.configureSettings(false);
        return true;
    }

    private boolean importConfiguration(boolean firstConfiguration) {
        try {
            var file = new FileInputStream("import.xml");
            var im = new XMLImport(file);

            XMLImport.ConfigurationXML config = null;

            try {
                config = im.parse();
            } catch (RuntimeException e) {
                this.view.error(COULD_NOT_PARSE_IMPORT_FILE);
                return true;
            }

            // importa le impostazioni (città, luoghi, giorni...)
            XMLImport.SettingsXML settings = config.settings;
            if (settings != null) {
                this.view.warn(LOADING_CONFIG);

                boolean err = false;
                int i;
                if (settings.city == null && firstConfiguration) err = true;
                if (settings.expiration <= 0) err = true;
                if (settings.locations == null || settings.locations.isEmpty()) err = true;
                if (settings.days == null || settings.days.isEmpty()) err = true;
                if (settings.intervals == null || settings.intervals.isEmpty()) err = true;

                if (err) {
                    this.view.error(IMPORT_FILE_NOT_VALID);
                    return true;
                }
                var cityOverride = this.model.configureSettings(settings.city, settings.expiration, settings.locations, settings.days, settings.intervals);
                if (cityOverride)
                    this.view.warn(CITY_NOT_OVERWRITABLE_SUCCESS);

            }

            if (config.hierarchies != null) {
                this.view.warn(IMPORTING_HIERARCHIES);
                // importa gerarchie
                config.hierarchies.forEach((e) -> {
                    try {
                        this.createHierarchyFromXML(e);
                        this.view.getInfoFactory().getMessageHierarchyImportFullfilled(e);
                    } catch (DuplicateCategoryException ex) {
                        this.view.error(DUPLICATE_CATEGORY);
                    } catch (InvalidCategoryException ex) {
                        this.view.error(INVALID_CATEGORY);
                    } catch (DuplicateFieldException ex) {
                        this.view.error(DUPLICATE_FIELD);
                    } catch (InvalidFieldException ex) {
                        this.view.error(INVALID_FIELD);
                    } catch (SQLException ex) {
                        this.view.error(ERR_IMPORTING_HIERARCHY);
                        ex.printStackTrace();
                    }
                });
            }
        } catch (FileNotFoundException e) {
            this.view.error(IMPORT_FILE_NOT_FOUND);
        } catch (XMLStreamException e) {
            this.view.error(ERR_IMPORTING_CONFIG);
        }
        return true;
    }

    /**
     * Data una gerarchia parsata dall'XML la salva
     *
     * @param hierarchyXML la definizione della gerarchia da creare
     */
    private void createHierarchyFromXML(XMLImport.HierarchyXML hierarchyXML) throws DuplicateCategoryException, InvalidCategoryException, DuplicateFieldException, InvalidFieldException, SQLException {
        // creazione della categoria root
        Category root = this.createCategoryFromXML(null, hierarchyXML.root);
        if (hierarchyXML.root.fields != null)
            for (var e : hierarchyXML.root.fields) {
                this.addFieldFromXML(root, e);
            }

        // aggiunge i campi default alla categoria radice
        root.put("Stato di conservazione", new TypeDefinition(true));
        root.put("Descrizione libera", new TypeDefinition(false));

        // associa una CategoryXML all'istanza della Category padre già creata
        var categoryStack = new LinkedList<AbstractMap.SimpleEntry<XMLImport.CategoryXML, Category>>();

        if (hierarchyXML.root.categories != null) {
            if (hierarchyXML.root.categories.size() == 1) throw new InvalidCategoryException();
            for (var e : hierarchyXML.root.categories) {
                categoryStack.add(new AbstractMap.SimpleEntry<>(e, root));
            }
        }

        // creazione delle categorie figlie
        while (categoryStack.size() != 0) {
            var toInsert = categoryStack.pop();
            Category father = toInsert.getValue();

            NodeCategory r = null;
            while (r == null) {
                var newChild = this.createCategoryFromXML(root, toInsert.getKey());
                r = this.appendCategory(father, newChild);

                if (toInsert.getKey().fields != null)
                    for (var f : toInsert.getKey().fields) {
                        this.addFieldFromXML(newChild, f);
                    }

                if (toInsert.getKey().categories != null) {
                    if (toInsert.getKey().categories.size() == 1) throw new InvalidCategoryException();
                    for (var e : toInsert.getKey().categories) {
                        categoryStack.add(new AbstractMap.SimpleEntry<>(e, newChild));
                    }
                }
            }
            // aggiorna i padri nello stack, serve perchè l'istanza potrebbe essere cambiata
            var newCategoryStack = new LinkedList<AbstractMap.SimpleEntry<XMLImport.CategoryXML, Category>>();
            for (var e : categoryStack) {
                if (e.getValue() == father) {
                    var newElem = new AbstractMap.SimpleEntry<>(e.getKey(), (Category) r);
                    newCategoryStack.add(newElem);
                } else {
                    newCategoryStack.add(e);
                }
            }
            categoryStack = newCategoryStack;

            if (father == root) root = r;
        }

        this.model.createHierarchy(root);
    }

    /**
     * Data una gerarchia importata da XML la salva
     *
     * @param root        categoria root della gerarchia
     * @param categoryXML la definizione della categoria da creare
     */
    private @NotNull Category createCategoryFromXML(Category root, XMLImport.CategoryXML categoryXML) throws DuplicateCategoryException, InvalidCategoryException {

        String name = categoryXML.name;
        if (name == null || name.length() == 0)
            throw new InvalidCategoryException();

        if (root == null && !this.model.isValidRootCategoryName(name))
            throw new DuplicateCategoryException();
        else if (root != null && !root.isValidChildCategoryName(name))
            throw new DuplicateCategoryException();

        String descr = categoryXML.description != null ? categoryXML.description : "";

        return this.model.createCategory(name, descr, null);
    }

    /**
     * Data una categoria e dei campi caricati da XML, li aggiunge alla categoria data
     *
     * @param c        categoria a cui aggiungere un campo
     * @param fieldXML il parametro da aggiungere
     */
    private void addFieldFromXML(Category c, XMLImport.FieldXML fieldXML) throws InvalidFieldException, DuplicateFieldException {
        // nome non deve essere già esistente tra i padri della categoria
        String name = fieldXML.name;
        if (name == null)
            throw new InvalidFieldException();
        if (name.length() == 0)
            throw new InvalidFieldException();
        if (c.containsKey(name))
            throw new DuplicateFieldException();
        c.put(name, new TypeDefinition(fieldXML.type, fieldXML.required));
    }

    //INTERNAL ACTIONS==============================================================

    /**
     * Permette di richiedere all'utente un'azione tramite un menu ed eseguire l'azione associata
     *
     * @param actions lista di azioni disponibili
     * @param prompt  messaggio da comunicare all'utente
     * @return booleano di ritorno dall'azione eseguita
     */
    private <T> T chooseAndRun(List<MenuAction<T>> actions, String prompt) {
        while (true) {
            var action = this.view.chooseOption(
                    actions.stream()
                            .filter(e -> e.isPermitted(this.currentUser.getUser()))
                            .collect(Collectors.toList()),
                    prompt);
            if (action != null)
                return action.getAction().run();
            else {
                this.view.error(ACTION_NOT_ALLOWED);
            }
        }
    }

    /**
     * Permette all'utente di effettuare il login
     *
     * @return true se l'utente si è autenticato
     */
    private boolean login() {
        var username = this.view.get("Utente");

        List<AuthMethod> authType;
        try {
            authType = this.model.authenticationType(username);
            if (authType.size() < 1) return false;
            for (var t : authType) {
                var authData = this.getLoginData(t.getClass().getName());
                this.currentUser = this.model.authenticate(t, authData);
                if (this.currentUser.isValid()) {
                    return true;
                } else {
                    this.view.error(USER_LOGIN_FAILED);
                    return false;
                }
            }
        } catch (InvalidUserException e) {
            this.view.error(USER_NOT_EXISTING);
        }

        return false;
    }

    /**
     * Richiede all'utente l'inserimento di una nuova password,
     * permettendogli di inserirla due volte per conferma
     *
     * @return password inserita
     */
    private @NotNull String getNewPassword() {
        String psw;
        while (true) {
            psw = this.view.getPassword("Nuova password");

            if (!psw.equals(this.view.getPassword("Ripeti la password"))) {
                this.view.error(PASSWORD_NOT_SAME);
                continue;
            }

            return psw;
        }
    }

    /**
     * In base al tipo di autenticazione, permetta al model di richiedere i dati necessari all'utente tramite la view
     *
     * @param method il metodo di autenticazione
     * @return dati relativi all'autenticazione
     */
    private @Nullable AuthData getLoginData(@NotNull String method) {
        if (method.equals(PasswordAuthMethod.class.getName()))
            return PasswordAuthMethod.getAuthData(this.view.getPassword());
        else {
            this.view.error(USER_NO_AUTH_METHOD);
            return null;
        }
    }

    /**
     * Permette la scelta riguardo l'aggiunta di campi alla categoria parametro
     *
     * @param c categoria a cui aggiungere i campi
     */
    private void makeFields(Category c) {
        while (this.chooseAndRun(Arrays.asList(
                new PrivilegedMenuAction<>("No, torna all'inserimento categorie", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, () -> false, false, 0, -1),
                new PrivilegedMenuAction<>("Sì, aggiungi campo nativo", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, () -> this.addField(c))
        ), "Si vuole aggiungere un campo nativo?")) ;
    }

    /**
     * Permette la creazione di un nuovo campo in una categoria
     *
     * @param c categoria a cui aggiungere un campo
     * @return true
     */
    private boolean addField(Category c) {
        // scelta del nome del campo, non deve essere già esistente tra i padri della categoria
        String name = null;
        while (name == null) {
            name = this.view.getText("Nome campo").trim();
            while (name.length() == 0) {
                this.view.warn(NO_EMPTY_NAME);
                name = this.view.getText("Nome campo").trim();
            }
            if (c.containsKey(name)) {
                this.view.error(DUPLICATE_FIELD_IN_CATEGORY);
                name = null;
            }
        }

        // se ci sono più tipi di campo possibili permette di sceglierlo
        // per ora è possibile inserire solo stringhe, quindi salta la scelta
        var type = TypeDefinition.TypeAssociation.values()[0];

        if (TypeDefinition.TypeAssociation.values().length >= 2) {
            type = this.view.chooseOption(
                    Arrays.stream(TypeDefinition.TypeAssociation.values())
                            .map(e -> (MenuAction<TypeDefinition.TypeAssociation>) new PrivilegedMenuAction<>(e.toString(), new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, () -> e))
                            .toList(), "Seleziona un tipo").getAction().run();
        }

        // chiede se la compilazione del campo è obbligatoria
        var required = this.view.get("Obbligatorio [y/N]").equalsIgnoreCase("y");

        c.put(name, new TypeDefinition(type, required));

        return true;
    }

    /**
     * Le categorie di default sono create come LeafCategory
     *
     * @return categoria creata
     */
    private @NotNull Category createCategory(Category root) throws DuplicateCategoryException {

        String name = this.view.getText("Nome").trim();
        while (name.length() == 0) {
            this.view.warn(NO_EMPTY_NAME);
            name = this.view.getText("Nome").trim();
        }
        if (root == null && !this.model.isValidRootCategoryName(name))
            throw new DuplicateCategoryException();
        else if (root != null && !root.isValidChildCategoryName(name))
            throw new DuplicateCategoryException();

        String description = this.view.getText("Descrizione").trim();

        return this.model.createCategory(name, description, null);
    }

    /**
     * Permette di aggiungere una categoria figlia ad una padre
     *
     * @param father categoria padre
     * @param child  categoria figlia da aggiungere
     * @return la nuova categoria padre
     */
    private @NotNull NodeCategory appendCategory(Category father, Category child) {
        var f = father instanceof LeafCategory ? ((LeafCategory) father).convertToNode() : (NodeCategory) father;
        f.addChild(child);
        return f;
    }

    /**
     * A partire da una categoria root, crea un menu che permette di scegliere una delle categorie figlie
     * Utilizzato per scegliere la categoria padre a cui aggiungere una categoria figlia
     * <p>
     * Passo base della ricorsione
     *
     * @param root categoria radice
     * @return la lista di MenuAction delle categorie figlie
     */
    @Contract("_ -> new")
    private @NotNull List<MenuAction<Category>> getCategorySelectionMenu(Category root) {
        return this.getCategorySelectionMenu(root, new LinkedList<>(), "");
    }

    /**
     * A partire da una categoria root, crea un menu che permette di scegliere una delle categorie figlie
     * Utilizzato per scegliere la categoria padre a cui aggiungere una categoria figlia
     * <p>
     * Passo ricorsivo, aggiunge le categorie figlie
     *
     * @param root   categoria radice
     * @param acc    lista a cui aggiungere le MenuAction
     * @param prefix prefisso da anteporre ai nomi delle categorie
     * @return la lista completa delle categorie figlie
     */
    private List<MenuAction<Category>> getCategorySelectionMenu(@NotNull Category root, @NotNull List<MenuAction<Category>> acc, String prefix) {
        acc.add(new MenuAction<>(prefix + root.getName(), () -> root));
        if (root instanceof NodeCategory)
            for (var child : ((NodeCategory) root).getChildren())
                this.getCategorySelectionMenu(child, acc, prefix + root.getName() + " > ");
        return acc;
    }

    /**
     * Richiede l'inserimento all'utente dei parametri di configurazione,
     * se è la prima configurazione permette di modificare anche la piazza
     *
     * @param firstConfiguration true se prima configurazione
     */
    private void configureSettings(boolean firstConfiguration) {

        String city;
        if (firstConfiguration) {
            boolean toImport = this.chooseAndRun(Arrays.asList(
                    new PrivilegedMenuAction<>("No, aggiungi la configurazione manualmente", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, () -> false, false, 0, -1),
                    new PrivilegedMenuAction<>("Sì, importa le configurazioni automaticamente", new User.UserTypes[]{User.UserTypes.CONFIGURATOR}, () -> true)
            ), "Si vuole importare la configurazione da file?");

            if (toImport) {
                this.importConfiguration(true);
                return;
            }
            city = this.view.get("Inserire la piazza di scambio");
        } else {
            this.view.warn(CITY_NOT_OVERWRITABLE);
            city = this.model.readSettings().getCity();
        }

        List<String> places = this.view.getStringList("Inserire un luogo", true);

        List<Settings.Day> days = this.view.getGenericList("Inserire un giorno", true,
                        (v) -> {
                            try {
                                return Settings.Day.fromString(v);
                            } catch (CannotParseDayException e) {
                                return null;
                            }
                        }).stream() // sort the days, so it does not depend on input order
                .sorted(it.matlice.ingsw.model.data.Settings.Day::compareTo)
                .toList();

        List<Interval> intervals = Interval.mergeIntervals(
                        this.view.getGenericList("Inserire un intervallo orario [es. 15:30-17:00]", true,
                                (v) -> {
                                    try {
                                        return Interval.fromString(v);
                                    } catch (CannotParseIntervalException | InvalidIntervalException e) {
                                        return null;
                                    }
                                })).stream() // sort the intervals after they have been merged together
                .sorted(Interval::compareTo)
                .toList();

        int daysDue = this.view.getInt("Inserire la scadenza (in numero di giorni)", (e) -> e > 0);

        if (firstConfiguration) {
            this.model.configureSettings(city, daysDue, places, days, intervals);
        } else {
            this.model.configureSettings(city, daysDue, places, days, intervals);
        }

    }

    /**
     * Premette all'utente di scegliere una categoria foglia
     *
     * @param message messaggio
     * @return la categoria foglia scelta
     */
    private LeafCategory chooseLeafCategory(String message) {
        return this.view.chooseOption(
                this.model.getLeafCategories().stream()
                        .map((e) -> new MenuAction<>(e.fullToString(), () -> e))
                        .collect(Collectors.toList()),
                message
        ).getAction().run();
    }

    /**
     * Permette di creare un nuovo articolo appartenente alla categoria data
     *
     * @param e categoria foglia a cui appartiene il nuovo articolo
     */
    private void addArticle(LeafCategory e) {

        Map<String, Object> fields = new HashMap<>();

        String name = this.view.getTrimmedLine("Inserire il nome del nuovo articolo", false);

        boolean needRequiredField;
        boolean saveArticle;
        do {
            // cerca se ci sono campi obbligatori da compilare
            needRequiredField = e.fullEntrySet()
                    .stream()
                    .filter((f) -> !fields.containsKey(f.getKey()))
                    .anyMatch((f) -> f.getValue().required());

            // crea la lista di campi compilabili
            var actions = e.fullEntrySet()
                    .stream()
                    .filter((f) -> !fields.containsKey(f.getKey()))
                    .map((k) -> new MenuAction<>(k.getKey() + (k.getValue().required() ? " [R]" : ""), () -> {
                        fields.put(k.getKey(), this.getFieldValue(k));
                        return false;
                    })).collect(Collectors.toCollection(ArrayList::new));

            if (actions.size() == 0) {
                break;
            }

            // aggiunge alla lista delle azioni l'azione che permette di salvare
            // è attiva solo se non ci sono campi obbligatori da compilare
            actions.add(0, new MenuAction<>("Salva articolo", () -> true, needRequiredField, 0, -1));

            // scelta del campo da compilare
            saveArticle = this.view.chooseOption(
                    actions,
                    "Scegliere quale campo si vuole compilare (oppure salva)"
            ).getAction().run();
        } while (needRequiredField || !saveArticle);

        try {
            this.model.createOffer(this.currentUser.getUser(), name, e, fields);
            this.view.warn(OFFER_CREATED);
        } catch (RequiredFieldConstrainException ex) {
            this.view.error(MISSING_FIELD);
        }
    }

    /**
     * Permette di compilare il campo k per l'articolo a
     *
     * @param k campo da compilare
     */
    private Object getFieldValue(Map.Entry<String, TypeDefinition> k) {

        // in base al tipo di campo da compilare lo richiede all'utente
        // per ora sono supportate solo le stringhe
        return switch (k.getValue().type()) {
            case STRING -> this.view.getText(String.format("Inserire il valore per il campo '%s'", k.getKey()));
            default -> throw new RuntimeException();
        };
    }

    /**
     * Show a list of offers
     *
     * @param prompt message
     * @param offers list of offers
     */
    private void showOffers(String prompt, List<Offer> offers) {
        if (offers.size() == 0) {
            this.view.warn(NO_OFFER_FOUND);
            return;
        }
        this.view.showList(prompt, offers);
    }

}

