package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.auth.AuthMethod;
import it.matlice.ingsw.model.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.model.auth.password.PasswordAuthenticable;
import it.matlice.ingsw.model.data.ConfiguratorUser;
import it.matlice.ingsw.model.data.impl.jdbc.db.UserDB;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ConfiguratorUserImpl extends ConfiguratorUser implements PasswordAuthenticable, UserImpl {

    private final UserDB dbData;

    public ConfiguratorUserImpl(UserDB from) {
        this.dbData = from;
    }

    public ConfiguratorUserImpl(String username) {
        this.dbData = new UserDB();
        this.dbData.setUsername(username);
        this.dbData.setType(UserTypes.CONFIGURATOR.getTypeRepresentation());
    }

    public UserDB getDbData() {
        return this.dbData;
    }

    @Override
    public String getUsername() {
        return this.dbData.getUsername();
    }

    @Override
    public Long getLastLoginTime() {
        return this.dbData.getLastAccess();
    }

    @Override
    public void setLastLoginTime(long time) {
        this.dbData.setLastAccess(time);
    }

    @Override
    public List<AuthMethod> getAuthMethods() {
        return Arrays.asList(new AuthMethod[]{
                new PasswordAuthMethod(this)
        });
    }

    @Override
    public byte[] getPasswordHash() {
        return Base64.getDecoder().decode(this.dbData.getPasswordHash());
    }

    @Override
    public byte[] getPasswordSalt() {
        return Base64.getDecoder().decode(this.dbData.getPasswordSalt());
    }


    @Override
    public void setPassword(byte[] password) {
        this.dbData.setPasswordHash(Base64.getEncoder().encodeToString(password));
    }

    @Override
    public void setSalt(byte[] salt) {
        this.dbData.setPasswordSalt(Base64.getEncoder().encodeToString(salt));
    }


}
