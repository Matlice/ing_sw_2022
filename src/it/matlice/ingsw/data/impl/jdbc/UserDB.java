package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class UserDB {
    @DatabaseField(id = true)
    private String username;

    @DatabaseField(canBeNull = false)
    private String password_hash;

    @DatabaseField(canBeNull = false)
    private String password_salt;

    @DatabaseField(canBeNull = false)
    private String type;

    @DatabaseField(canBeNull = true)
    private Long lastAccess;

    public UserDB() {
    }

    public Long getLastAccess() {
        return this.lastAccess;
    }

    public void setLastAccess(Long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return this.password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getPassword_salt() {
        return this.password_salt;
    }

    public void setPassword_salt(String password_salt) {
        this.password_salt = password_salt;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
