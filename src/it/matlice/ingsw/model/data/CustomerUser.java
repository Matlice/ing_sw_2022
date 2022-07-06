package it.matlice.ingsw.model.data;

import java.util.Arrays;
import java.util.List;

/**
 * Rappresenta un utente fruitore
 */
public abstract class CustomerUser extends User{

    @Override
    public List<UserTypes> getAuthTypes(){
        return List.of(UserTypes.CUSTOMER);
    }

    @Override
    public Boolean isAdmin(){
        return false;
    }

}
