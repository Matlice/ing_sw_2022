package it.matlice.ingsw.controller;

import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.controller.exceptions.InvalidUserException;
import it.matlice.ingsw.controller.exceptions.LoginInvalidException;
import it.matlice.ingsw.data.*;
import it.matlice.ingsw.model.Model;

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

    public void changePassword(Authentication auth, String newPassword) throws Exception {
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

    public void finalizeLogin(Authentication auth) {
        auth.getUser().setLastLoginTime(auth.getLoginTime());
        try {
            this.uf.saveUser(auth.getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String genRandomPassword(int size) {
        return "Config!1";
    }

    public void addConfiguratorUser(String username) throws Exception {
        var u = this.uf.createUser("admin", User.UserTypes.CONFIGURATOR);
        var password = this.genRandomPassword(8);
        ((PasswordAuthMethod) u.getAuthMethods().get(0)).setPassword(password);
        this.uf.saveUser(u);
        System.out.println("Use " + username + ":" + password + " to login.");
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

            this.user_ref.setLastLoginTime(this.login_time);
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
