package it.matlice.ingsw.controller;

import it.matlice.ingsw.data.*;

import java.util.List;

import static it.matlice.ingsw.controller.Settings.LOGIN_EXPIRATION_TIME;

public class Controller {

    private final HierarchyFactory hf;
    private final CategoryFactory cf;
    private final UserFactory uf;

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

    /**
     * Effettua l'autenticazione
     *
     * @param username username dell'utente
     * @return un token di sessione oppure null se l'auitenticazione non ha avuto successo
     */
    public Authentication authenticate(String username) {
        //todo get user from factory

        //todo see(Algoritmo verifica delle credenziali)
        // ottieni i metodi di login e iterando su di essi, in funzione del tipo di login esegui una funzione per prendere i dati da stdin
        // quindi chiama performAuthentication e se positivo ritorna l'istanza di AuthImpl.
        // la chiamata a performAuthentication deve prevedere il cambio password immediato se è il primo login dell'utente.

        return new AuthImpl(null);
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
