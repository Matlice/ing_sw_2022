package it.matlice.ingsw.controller;

import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.controller.exceptions.InvalidUserException;
import it.matlice.ingsw.controller.exceptions.LoginInvalidException;
import it.matlice.ingsw.data.*;
import it.matlice.ingsw.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static it.matlice.ingsw.controller.Settings.LOGIN_EXPIRATION_TIME;

public class Controller {

    private final HierarchyFactory hf;
    private final CategoryFactory cf;
    private final UserFactory uf;
    private Model model;

    private List<Hierarchy> hierarchies;

    public Controller(HierarchyFactory hf, CategoryFactory cf, UserFactory uf) {
        this.hf = hf;
        this.cf = cf;
        this.uf = uf;

        try {
            this.hierarchies = hf.getHierarchies();
        } catch (Exception e) {
            e.printStackTrace(); //todo handle
        }
    }

    public void changePassword(@NotNull Authentication auth, String newPassword) throws Exception {
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
            var authdata = this.model.getLoginData(method.getClass().getName());
            if (authdata == null)
                continue;
            ret = method.performAuthentication(authdata);
            if (ret) break;
        }

        if (ret)
            return new AuthImpl(user);
        return null;
    }

    public void finalizeLogin(@NotNull Authentication auth) throws Exception{
        auth.getUser().setLastLoginTime(auth.getLoginTime());
        this.uf.saveUser(auth.getUser());
    }

    @Contract(pure = true)
    private @NotNull String genRandomPassword() {
        return "Config!1";
    }

    public String addConfiguratorUser(String username) throws Exception {
        var u = this.uf.createUser(username, User.UserTypes.CONFIGURATOR);
        var password = this.genRandomPassword();
        ((PasswordAuthMethod) u.getAuthMethods().get(0)).setPassword(password);
        this.uf.saveUser(u);
        return password;
    }

    public Category createCategory(String name, Category father) {
        try {
            return this.cf.createCategory(name, father, true);
        } catch (Exception e) {
            return null;
        }
    }

    public void createHierarchy(Category root) {
        try {
            this.cf.saveCategory(root);
            //todo check if no same name
            this.hierarchies.add(this.hf.createHierarchy(root));
        } catch (Exception e) {
        }
    }

    public boolean isCategoryValid(Category c) {
        if (c instanceof LeafCategory)
            return true;
        assert c instanceof NodeCategory;
        return ((NodeCategory) c).getChildren().length >= 2 && Arrays.stream(((NodeCategory) c).getChildren()).allMatch(this::isCategoryValid);
    }

    public void setModel(Model m) {
        this.model = m;
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
