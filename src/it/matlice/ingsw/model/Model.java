package it.matlice.ingsw.model;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.AuthMethod;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.data.factories.CategoryFactory;
import it.matlice.ingsw.data.factories.HierarchyFactory;
import it.matlice.ingsw.data.factories.UserFactory;
import it.matlice.ingsw.model.exceptions.DuplicateUserException;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.InvalidUserTypeException;
import it.matlice.ingsw.model.exceptions.LoginInvalidException;
import it.matlice.ingsw.data.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;

import static it.matlice.ingsw.auth.password.PasswordAuthMethod.isPasswordValid;
import static it.matlice.ingsw.model.Settings.LOGIN_EXPIRATION_TIME;

/**
 * Model dell'applicazione, fornisce i metodi per accedere ai dati
 */
public class Model {

    private final HierarchyFactory hf;
    private final CategoryFactory cf;
    private final UserFactory uf;

    private List<Hierarchy> hierarchies;

    /**
     * Costruttore del Model
     * @param hf la hierarchy factory che permette di interfacciarsi col DB per le gerarchie
     * @param cf la category factory che permette di interfacciarsi col DB per le categorie
     * @param uf la user factory che permette di interfacciarsi col DB per gli utenti
     */
    public Model(@NotNull HierarchyFactory hf, @NotNull  CategoryFactory cf, @NotNull UserFactory uf) {
        this.hf = hf;
        this.cf = cf;
        this.uf = uf;

        try {
            this.hierarchies = hf.getHierarchies();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Permette il cambio password all'utente
     *
     * @param auth autenticazione, permette di indetificare l'utente e verificare sia loggato
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
     * @param auth autenticazione, permette di indetificare l'utente e verificare sia loggato
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

        ((PasswordAuthMethod) u.getAuthMethods().get(0)).setPassword(password, true); //todo assert type
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
        ((PasswordAuthMethod) u.getAuthMethods().get(0)).setPassword(password, false); //todo assert type
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
        return !this.hierarchies.stream()
                .map((e) -> e.getRootCategory().getName())
                .toList()
                .contains(name);
    }

    /**
     * Crea una nuova gerarchia con la categoria root specificata
     *
     * @param root categoria root della gerarchia
     * @throws SQLException errore di connessione col database
     */
    public void createHierarchy(Category root) throws SQLException {
        this.cf.saveCategory(root);
        this.hierarchies.add(this.hf.createHierarchy(root));
    }

    /**
     * Ritorna le gerarchie
     * @return lista di gerarchie a sistema
     */
    public List<Hierarchy> getHierarchies() {
        return this.hierarchies;
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
