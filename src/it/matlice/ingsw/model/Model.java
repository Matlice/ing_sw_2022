package it.matlice.ingsw.model;

import it.matlice.ingsw.controller.MenuAction;
import it.matlice.ingsw.model.auth.AuthData;
import it.matlice.ingsw.model.auth.AuthMethod;
import it.matlice.ingsw.model.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.model.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.model.data.factories.*;
import it.matlice.ingsw.model.data.impl.jdbc.MessageFactoryImpl;
import it.matlice.ingsw.model.exceptions.*;
import it.matlice.ingsw.model.data.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.*;

import static it.matlice.ingsw.model.auth.password.PasswordAuthMethod.isPasswordValid;
import static it.matlice.ingsw.model.Settings.LOGIN_EXPIRATION_TIME;

/**
 * Model dell'applicazione, fornisce i metodi per accedere ai dati
 */
public class Model {

    private final HierarchyFactory hf;
    private final CategoryFactory cf;
    private final UserFactory uf;
    private final SettingsFactory sf;
    private final OfferFactory of;
    private final MessageFactory mf;

    private it.matlice.ingsw.model.data.Settings settings = null;

    /**
     * Costruttore del Model
     * @param hf la hierarchy factory che permette di interfacciarsi col DB per le gerarchie
     * @param cf la category factory che permette di interfacciarsi col DB per le categorie
     * @param uf la user factory che permette di interfacciarsi col DB per gli utenti
     * @param sf la settings factory che permette di interfacciarsi col DB per i parametri di configurazione
     * @param af la article factory che permette di interfacciarsi col DB per gli articoli
     */
    public Model(@NotNull HierarchyFactory hf, @NotNull CategoryFactory cf, @NotNull UserFactory uf, @NotNull SettingsFactory sf, @NotNull OfferFactory af, @NotNull MessageFactory mf) {
        this.hf = hf;
        this.cf = cf;
        this.uf = uf;
        this.sf = sf;
        this.of = af;
        this.mf = mf;
    }

    /**
     * Permette il cambio password all'utente
     *
     * @param auth autenticazione, permette di identificare l'utente e verificare sia loggato
     * @param newPassword nuova password che si vuole impostare
     * @throws SQLException errore di connessione col database
     * @throws InvalidPasswordException password non rispettante i requisiti di sicurezza
     * @throws LoginInvalidException utente non loggato
     */
    public void changePassword(@NotNull Authentication auth, String newPassword) throws SQLException, InvalidPasswordException, LoginInvalidException {
        if (!auth.isValid())
            throw new LoginInvalidException();

        for (var method : auth.getUser().getAuthMethods()) {
            if (method instanceof PasswordAuthMethod)
                ((PasswordAuthMethod) method).setPassword(newPassword);
        }
        this.uf.saveUser(auth.getUser());
    }

    /**
     * Ritorna una lista di metodi disponibili per l'utente
     *
     * @param username username dell'utente
     * @return lista di metodi disponibili
     */
    public List<AuthMethod> authenticationType(String username) throws InvalidUserException {
        User user;
        try {
            user = this.uf.getUser(username);
        } catch (Exception e) {
            throw new InvalidUserException();
        }

        return user.getAuthMethods();
    }

    /**
     * Dati i parametri di autenticazione, ritorna il token di autenticazione
     *
     * @param method metodo di autenticazione
     * @param data parametri di autenticazione
     * @return token di autenticazione
     */
    public Authentication authenticate(@NotNull AuthMethod method, AuthData data) {

        assert method.getUser() instanceof User;

        if (method.performAuthentication(data)) {
            return new AuthImpl((User) method.getUser());
        }

        return null;
    }

    /**
     * Ultimo passaggio per completare il login
     * Imposta l'ultimo accesso dell'utente
     *
     * @param auth autenticazione, permette di identificare l'utente e verificare sia loggato
     * @throws SQLException errore di connessione col database
     */
    public void finalizeLogin(@NotNull Authentication auth) throws SQLException {
        auth.getUser().setLastLoginTime(auth.getLoginTime());
        this.uf.saveUser(auth.getUser());
    }

    /**
     * Genera una password casuale di 8 caratteri alfanumerici
     *
     * @return la password generata
     */
    @Contract(pure = true)
    private @NotNull String genRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        while (password.length() < 8) {
            char character = (char) random.nextInt(Character.MAX_VALUE);
            if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || (character >= '0' && character <= '9')) {
                password.append(character);
            }
        }
        return password.toString();
    }

    /**
     * Crea un nuovo utente del tipo specificato
     * @param username
     * @param type
     * @return
     * @throws SQLException
     * @throws DuplicateUserException
     * @throws InvalidUserTypeException
     */
    private User createUser(String username, User.UserTypes type) throws SQLException, DuplicateUserException, InvalidUserTypeException {
        if (this.uf.doesUserExist(username)) {
            throw new DuplicateUserException();
        }

        return this.uf.createUser(username, type);
    }

    /**
     * Aggiunge un utente configuratore
     *
     * @param username username dell'utente da creare
     * @param defaultPassword true per utilizzare una password default, altrimenti la genera casualmente
     * @return password dell'utente appena creato
     * @throws SQLException errore di connessione col database
     * @throws DuplicateUserException utente con username già esistente
     * @throws InvalidPasswordException
     * @throws InvalidUserTypeException
     */
    public String addConfiguratorUser(String username, boolean defaultPassword) throws SQLException, DuplicateUserException, InvalidUserTypeException, InvalidPasswordException {

        var u = this.createUser(username, User.UserTypes.CONFIGURATOR);

        String password;
        if (defaultPassword) {
            password = "Config!1";
        } else {
            password = this.genRandomPassword();
        }

        assert u.getAuthMethods().get(0) instanceof PasswordAuthMethod;
        ((PasswordAuthMethod) u.getAuthMethods().get(0)).setPassword(password, true);
        this.uf.saveUser(u);
        return password;
    }

    /**
     * Aggiunge un nuovo utente fruitore
     * @param username username dell'utente da creare
     * @param password password associata all'utente da creare
     * @throws SQLException errore di connessione col database
     * @throws DuplicateUserException utente con username già esistente
     * @throws InvalidUserTypeException
     * @throws InvalidPasswordException
     */
    public void registerUser(String username, String password) throws SQLException, DuplicateUserException, InvalidUserTypeException, InvalidPasswordException {
        // controllo effettuato a priori, altrimenti crea un utente senza password
        if (!isPasswordValid(password)) throw new InvalidPasswordException();

        var u = this.createUser(username, User.UserTypes.CUSTOMER);
        assert u.getAuthMethods().get(0) instanceof PasswordAuthMethod;
        ((PasswordAuthMethod) u.getAuthMethods().get(0)).setPassword(password, false);
        u.setLastLoginTime(0);
        this.uf.saveUser(u);
    }

    /**
     * Crea una nuova categoria foglia con i parametri passati
     *
     * @param name nome della categoria
     * @param description descrizione della categoria
     * @param father padre della categoria (null se è categoria root)
     * @return
     */
    public @NotNull Category createCategory(String name, String description, Category father) {
        return this.cf.createCategory(name, description, father, true);
    }

    /**
     * Controlla l'esistenza di una categoria radice il nome passato,
     * non possono esserci due categorie root con lo stesso nome
     *
     * @param name nome della categoria radice
     * @return true se il nome della categoria radice non è già esistente
     */
    public boolean isValidRootCategoryName(String name) {
        try {
            return !this.hf.getHierarchies().stream()
                    .map((e) -> e.getRootCategory().getName())
                    .toList()
                    .contains(name);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    /**
     * Crea una nuova gerarchia con la categoria root specificata
     *
     * @param root categoria root della gerarchia
     * @throws SQLException errore di connessione col database
     */
    public void createHierarchy(Category root) throws SQLException {
        this.cf.saveCategory(root);
        this.hf.getHierarchies().add(this.hf.createHierarchy(root));
    }

    /**
     * Ritorna le gerarchie
     * @return lista di gerarchie a sistema
     */
    public List<Hierarchy> getHierarchies() {
        try {
            return this.hf.getHierarchies();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Ritorna una lista con tutte le categorie foglia di ogni gerarchia
     * @return lista di categorie
     */
    public List<LeafCategory> getLeafCategories() {
        return this.getHierarchies()
                .stream()
                .map((e) -> e.getRootCategory().getChildLeafs())
                .flatMap(List::stream)
                .toList();
    }

    /**
     * Ritorna true se sono stati configurati i parametri dell'applicazione
     * (piazza, scadenza, giorni, orari, luoghi)
     * @return boolean
     */
    public boolean hasConfiguredSettings() {
        try {
            return this.sf.readSettings() != null;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    /**
     * Ritorna i parametri di configurazione attuali
     */
    public it.matlice.ingsw.model.data.Settings readSettings() {
        try {
            if(this.settings == null) this.settings = this.sf.readSettings();
            return this.settings;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Imposta i parametri di configurazione
     * @param city piazza di scambio
     * @param daysDue giorni di scadenza
     * @param locations luoghi
     * @param days giorni
     * @param intervals intervalli
     */
    public void configureSettings(String city, int daysDue, List<String> locations, List<it.matlice.ingsw.model.data.Settings.Day> days, List<Interval> intervals) {
        try {
            it.matlice.ingsw.model.data.Settings set = this.sf.readSettings();
            if (set == null) {
                this.sf.makeSettings(city, daysDue, locations, days, intervals);
            } else {
                assert set.getCity() != null;
                assert city.equals(set.getCity());

                // remove old locations, days and intervals
                this.sf.removeLocations(set);
                this.sf.removeDays(set);
                this.sf.removeIntervals(set);

                // add the new configuration
                this.sf.setDue(set, daysDue);
                for(var l: locations) this.sf.addLocation(set, l);
                for(var d: days) this.sf.addDay(set, d);
                for(var i: intervals) this.sf.addInterval(set, i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Crea un nuovo articolo
     * @param e categoria a cui appartiene l'articolo da creare
     * @return articolo creato
     */
    public Offer createOffer(User u, String name, LeafCategory e, Map<String, Object> fields) throws RequiredFieldConstrainException {
        try {
            return this.of.makeOffer(name, u, e, fields);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Ritorna la lista delle offerte relative all'utente
     * @param user utente
     * @return liste di offerte dell'utente
     */
    public List<Offer> getOffersByUser(User user) {
        try {
            return this.of.getOffers(user);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Ritorna la lista delle offerte di una certa categoria
     * @param cat categoria
     * @return liste di offerte dell'utente
     */
    public List<Offer> getOffersByCategory(LeafCategory cat) {
        try {
            return this.of.getOffers(cat);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Ritorna una lista di offerte che possono essere ritirate dall'utente
     * Possono essere ritirate le offerte in stato non RETRACTED
     * @param user utente
     * @return lista di offerte ritirabili
     */
    public List<Offer> getRetractableOffers(User user) {
        return this.getOffersByUser(user)
                .stream()
                .filter((o) -> o.getStatus() != Offer.OfferStatus.RETRACTED)
                .toList();
    }

    /**
     * Ritira un'offerta
     * @param offerToRetract offerta da ritirare
     */
    public void retractOffer(Offer offerToRetract) {
        try {
            if (offerToRetract.getStatus() != Offer.OfferStatus.RETRACTED)
                this.of.setOfferStatus(offerToRetract, Offer.OfferStatus.RETRACTED);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Ritorna una lista di aticoli dell'utente disponibili allo scambio,
     * ovvero la lista delle sue offerte aperte
     * @param owner proprietario
     * @return lista di offerte scambiabili
     */
    public List<Offer> getTradableOffers(User owner) {
        try {
            return this.of.getOffers(owner)
                    .stream()
                    .filter((e) -> e.getStatus().equals(Offer.OfferStatus.OPEN))
                    .toList();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Ritorna una lisa di articoli scambiabili con l'articolo dato,
     * ovvero tutte le offerte aperte aperte di altri utenti appartenenti
     * alla stessa caategoria foglia dell'offerta data
     * @param offerToTrade offerta da scambiaare
     * @return lista di offerte scambiabili
     */
    public List<Offer> getTradableOffers(Offer offerToTrade) {
        try {
            return this.of.getOffers(offerToTrade.getCategory())
                    .stream()
                    .filter((e) -> !e.getOwner().equals(offerToTrade.getOwner()))
                    .filter((e) -> e.getStatus().equals(Offer.OfferStatus.OPEN))
                    .toList();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Aggiunge una proposta di scambio tra due offerte
     * @param offerToTrade articolo dell'utente che propone lo scambio
     * @param offerToAccept articolo richiesto in cambio
     */
    public void createTradeOffer(Offer offerToTrade, Offer offerToAccept) throws InvalidTradeOfferException {
        if (offerToTrade.getStatus() != Offer.OfferStatus.OPEN) throw new InvalidTradeOfferException();
        if (offerToAccept.getStatus() != Offer.OfferStatus.OPEN) throw new InvalidTradeOfferException();
        if (!offerToTrade.getCategory().equals(offerToAccept.getCategory())) throw new InvalidTradeOfferException();
        if (offerToTrade.getOwner().equals(offerToAccept.getOwner())) throw new InvalidTradeOfferException();

        try {
            this.of.createTradeOffer(offerToTrade, offerToAccept);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // todo far ritornare aperte entrambe dopo scadenza

    }

    public void timeIteration() {
        try {
            this.of.checkForDueDate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<Offer> getSelectedOffers(Authentication auth){
        try {
            return this.of.getSelectedOffers(auth.getUser());
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public List<Message> getUserMessages(User user) {
        try {
            return this.mf.getUserMessages(user);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Permette di accettare una proposta di scambio,
     * avanzando una prima proposta iniziale di giorno, data e ora
     * @param offer offerta da accettare
     * @param location proposta di luogo di scambio
     * @param day proposta di giorno di scambio
     * @param time proposta di ora di scambio
     * @return momento dello scambio
     */
    public Calendar acceptTrade(Offer offer, String location, it.matlice.ingsw.model.data.Settings.Day day, Interval.Time time) {
        assert offer.getStatus() == Offer.OfferStatus.SELECTED;
        try {
            var date = convertToDate(day, time);
            this.of.acceptTradeOffer(offer, this.mf, location, date);
            return date;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static Calendar convertToDate(it.matlice.ingsw.model.data.Settings.Day day, Interval.Time time) {
        var d = nextDayOfWeek(day.getCalendarDay());
        d.set(Calendar.HOUR_OF_DAY, time.getHour());
        d.set(Calendar.MINUTE, time.getMinute());
        return d;
    }

    private static Calendar nextDayOfWeek(int dow) {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        int diff = dow - date.get(Calendar.DAY_OF_WEEK);
        if (diff <= 0) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }

    /**
     * Classe che gestisce l'autenticazione dell'utente
     */
    private static class AuthImpl implements Authentication {
        private final User user_ref;
        private final long login_time;

        /**
         * Costruttore di AuthImpl
         * @param user_ref l'utente di cui si vuol creare l'autenticazione
         */
        private AuthImpl(User user_ref) {
            this.user_ref = user_ref;
            this.login_time = System.currentTimeMillis() / 1000L;
        }


        /**
         * @return Ritorna l'utente al quale il login è associato
         */
        @Override
        public User getUser() {
            return this.user_ref;
        }

        /**
         * Ottiene la data di login
         *
         * @return unix time stamp del login
         */
        @Override
        public long getLoginTime() {
            return this.login_time;
        }

        /**
         * Ottiene la data entro cui il login è valido
         *
         * @return unix time stamp della data di scadenza della sessione
         */
        @Override
        public long getExpirationTime() {
            return this.login_time + LOGIN_EXPIRATION_TIME;
        }

        /**
         * @return true se la sessione è valida
         */
        @Override
        public boolean isValid() {
            return this.loginProblem() == null;
        }

        /**
         * @return una stringa che identifica l'errore di sessione o null se la sessione è valida
         */
        @Override
        public String loginProblem() {
            if (System.currentTimeMillis() / 1000L >= this.getExpirationTime())
                return "Expired token";
            return null;
        }
    }
}
