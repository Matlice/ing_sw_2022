package it.matlice.ingsw.model;

import com.j256.ormlite.stmt.query.In;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.model.exceptions.DuplicateUserException;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.InvalidUserTypeException;
import it.matlice.ingsw.model.exceptions.LoginInvalidException;
import it.matlice.ingsw.data.*;
import it.matlice.ingsw.controller.Controller;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static it.matlice.ingsw.model.Settings.LOGIN_EXPIRATION_TIME;

public class Model {

    private final HierarchyFactory hf;
    private final CategoryFactory cf;
    private final UserFactory uf;
    private Controller controller;

    private List<Hierarchy> hierarchies;

    public Model(HierarchyFactory hf, CategoryFactory cf, UserFactory uf) {
        this.hf = hf;
        this.cf = cf;
        this.uf = uf;

        try {
            this.hierarchies = hf.getHierarchies();
        } catch (SQLException e) {
            e.printStackTrace(); //todo handle
        }
    }

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
     * Effettua l'autenticazione
     *
     * @param username username dell'utente
     * @return un token di sessione oppure null se l'auitenticazione non ha avuto successo
     */
    public Authentication authenticate(String username) throws InvalidUserException {
        User user;
        try {
            user = this.uf.getUser(username);
        } catch (Exception e) {
            throw new InvalidUserException();
        }

        boolean ret = false;
        for (var method : user.getAuthMethods()) {
            var authdata = this.controller.getLoginData(method.getClass().getName());
            if (authdata == null)
                continue;
            ret = method.performAuthentication(authdata);
            if (ret) break;
        }

        if (ret)
            return new AuthImpl(user);
        return null;
    }

    public void finalizeLogin(@NotNull Authentication auth) throws SQLException {
        auth.getUser().setLastLoginTime(auth.getLoginTime());
        this.uf.saveUser(auth.getUser());
    }

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

    public String addConfiguratorUser(String username, boolean defaultPassword) throws SQLException, DuplicateUserException, InvalidUserTypeException, InvalidPasswordException {

        if (this.uf.doesUserExist(username)) {
            throw new DuplicateUserException();
        }

        var u = this.uf.createUser(username, User.UserTypes.CONFIGURATOR);

        String password;
        if (defaultPassword) {
            password = "Config!1";
        } else {
            password = this.genRandomPassword();
        }
        ((PasswordAuthMethod) u.getAuthMethods().get(0)).setPassword(password, true);
        this.uf.saveUser(u);
        return password;
    }

    public Category createCategory(String name, String description, Category father) {
        return this.cf.createCategory(name, description, father, true);
    }

    public void createHierarchy(Category root) throws Exception{
        this.cf.saveCategory(root);
        this.hierarchies.add(this.hf.createHierarchy(root));
    }

    public List<Hierarchy> getHierarchies() {
        return this.hierarchies;
    }

    public void setController(Controller m) {
        this.controller = m;
    }

    public boolean isValidRootCategoryName(String name) {
        return !this.hierarchies.stream()
                .map((e) -> e.getRootCategory().getName())
                .collect(Collectors.toList())
                .contains(name);
    }


    private static class AuthImpl implements Authentication {
        private final User user_ref;
        private final long login_time;

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
