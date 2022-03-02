package it.matlice.ingsw.controller;

import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
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

    private List<Hierarchy> hierarchies;

    private static Controller instance;

    private Controller(HierarchyFactory hf, CategoryFactory cf, UserFactory uf) {
        this.hf = hf;
        this.cf = cf;
        this.uf = uf;

        try {
            this.hierarchies = hf.getHierarchies();
        } catch (Exception e) {
            e.printStackTrace(); //todo handle
        }
    }

    public static Controller getInstance(){
        return Controller.instance;
    }

    public static Controller makeInstance(HierarchyFactory hf, CategoryFactory cf, UserFactory uf){
        assert instance == null;
        instance = new Controller(hf, cf, uf);
        return Controller.instance;
    }

    public void changePassword(Authentication auth, String newPassword) throws LoginInvalidException, InvalidPasswordException {
        if(!auth.isValid())
            throw new LoginInvalidException();

        for(var method : auth.getUser().getAuthMethods()){
            if(method instanceof PasswordAuthMethod)
                ((PasswordAuthMethod) method).setPassword(newPassword);
        }
    }

    /**
     * Effettua l'autenticazione
     *
     * @param username username dell'utente
     * @return un token di sessione oppure null se l'auitenticazione non ha avuto successo
     */
    public Authentication authenticate(String username) throws InvalidUserException{
        User user;
        try {
            user = uf.getUser(username);
        } catch (Exception e){
            throw new InvalidUserException();
        }

        boolean ret = false;
        for(var method: user.getAuthMethods()){
            var authdata = Model.getInstance().getLoginData(method.getClass().getName());
            if(authdata == null)
                continue;
            ret = method.performAuthentication(authdata);
            if(ret) break;
        }

        if(ret)
            return new AuthImpl(user);
        return null;
    }

    public void mainloop(){
        Model.getInstance().login();
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
