package it.matlice.ingsw.model.data;

import java.util.List;

/**
 * la classe rappresenta un utente con privilegi di configurazione
 */
public abstract class ConfiguratorUser extends User {
    public List<UserTypes> getAuthTypes(){
        return List.of(UserTypes.CONFIGURATOR);
    }
}
