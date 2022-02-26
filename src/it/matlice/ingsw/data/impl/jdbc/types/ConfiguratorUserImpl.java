package it.matlice.ingsw.data.impl.jdbc.types;

import it.matlice.ingsw.auth.AuthMethod;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.auth.password.PasswordAuthenticable;
import it.matlice.ingsw.data.ConfiguratorUser;
import it.matlice.ingsw.data.impl.jdbc.UserDB;

import java.util.Arrays;
import java.util.List;

public class ConfiguratorUserImpl extends ConfiguratorUser implements PasswordAuthenticable {

    private final UserDB dbData;

    public ConfiguratorUserImpl(UserDB from) {
        this.dbData = from;
    }

    public ConfiguratorUserImpl(String username) {
        this.dbData = new UserDB();
        this.dbData.setUsername(username);
        this.dbData.setType(UserTypes.CONFIGURATOR.getTypeRepresentation());

        //todo real password management
        this.dbData.setPassword_hash("");
        this.dbData.setPassword_salt("");
    }

    public UserDB getDbData() {
        return this.dbData;
    }

    @Override
    public String getUsername() {
        return this.dbData.getUsername();
    }

    @Override
    public List<AuthMethod> getAuthMethods() {
        return Arrays.asList(new AuthMethod[]{
                new PasswordAuthMethod(this)
        });
    }

    @Override
    public String getPasswordHash() {
        return this.dbData.getPassword_hash();
    }

    @Override
    public String getPasswordSalt() {
        return this.dbData.getPassword_salt();
    }

    @Override
    public void setPassword(String password) throws InvalidPasswordException {
        //todo
    }
}
