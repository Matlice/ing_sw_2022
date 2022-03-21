package it.matlice.ingsw.data.impl.jdbc.types;

import it.matlice.ingsw.auth.AuthMethod;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.auth.password.PasswordAuthenticable;
import it.matlice.ingsw.data.ConfiguratorUser;
import it.matlice.ingsw.data.StandardUser;
import it.matlice.ingsw.data.impl.jdbc.UserDB;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class StandardUserImpl extends StandardUser implements PasswordAuthenticable, UserImpl {

    private final UserDB dbData;

    public StandardUserImpl(UserDB from) {
        this.dbData = from;
    }

    public StandardUserImpl(String username) {
        this.dbData = new UserDB();
        this.dbData.setUsername(username);
        this.dbData.setType(UserTypes.STANDARD.getTypeRepresentation());
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
