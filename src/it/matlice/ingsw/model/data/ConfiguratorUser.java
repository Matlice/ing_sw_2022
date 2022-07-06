package it.matlice.ingsw.model.data;

import java.util.List;

/**
 * la classe rappresenta un utente con privilegi di configurazione
 */
public abstract class ConfiguratorUser extends User {

    @Override
    public List<UserTypes> getAuthTypes(){
        return List.of(UserTypes.CONFIGURATOR);
    }

    @Override
    public Boolean isAdmin(){
        return true;
    }

}
