package it.matlice.ingsw.data.impl.sqlite.types;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.AuthMethod;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.auth.password.PasswordAuthenticable;
import it.matlice.ingsw.data.ConfiguratorUser;
import it.matlice.ingsw.data.UserTypes;
import it.matlice.ingsw.data.impl.sqlite.UserDB;

public class ConfiguratorUserImpl extends ConfiguratorUser implements PasswordAuthenticable {

    private final UserDB dbData;

    public UserDB getDbData() {
        return dbData;
    }

    public ConfiguratorUserImpl(UserDB from){
        this.dbData = from;
    }

    public ConfiguratorUserImpl(String username, String password){
        dbData = new UserDB();
        dbData.setUsername(username);
        dbData.setType(UserTypes.CONFIGURATOR.getTypeRepresentation());

        //todo real password management
    }

    @Override
    public String getUsername() {
        return this.dbData.getUsername();
    }

    @Override
    public boolean authenticate(AuthMethod method, AuthData authdata) {
        return method.performAuthentication(authdata);
    }

    @Override
    public AuthMethod[] getAuthMethods() {
        return new AuthMethod[]{
                new PasswordAuthMethod(this)
        };
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
    public String setPassword(String password) {
        //todo
    }
}
