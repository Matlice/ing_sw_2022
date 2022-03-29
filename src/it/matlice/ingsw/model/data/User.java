package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.auth.AuthMethod;
import it.matlice.ingsw.model.auth.Authenticable;

import java.util.List;

/**
 * rappresenta un utente del sistema
 */
public abstract class User implements Authenticable {
    public abstract String getUsername();

    public abstract Long getLastLoginTime();

    public abstract void setLastLoginTime(long time);

    @Override
    public abstract List<AuthMethod> getAuthMethods();


    public enum UserTypes {
        CONFIGURATOR("configurator"),
        CUSTOMER("customer");

        private final String typeRepresentation;

        UserTypes(String typeRepresentation) {
            this.typeRepresentation = typeRepresentation;
        }

        public String getTypeRepresentation() {
            return this.typeRepresentation;
        }
    }

}
