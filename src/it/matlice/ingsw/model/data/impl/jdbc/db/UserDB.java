package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Classe che rappresenta a database la tabella `users`
 */
@DatabaseTable(tableName = "users")
public class UserDB {
    @DatabaseField(id = true)
    private String username;

    @DatabaseField(canBeNull = false)
    private String password_hash = "";

    @DatabaseField(canBeNull = false)
    private String password_salt = "";

    @DatabaseField(canBeNull = false)
    private String type;

    @DatabaseField()
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

    public String getPasswordHash() {
        return this.password_hash;
    }

    public void setPasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getPasswordSalt() {
        return this.password_salt;
    }

    public void setPasswordSalt(String password_salt) {
        this.password_salt = password_salt;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserDB)) return false;
        if (this.username == null) return false;
        return this.username.equals(((UserDB) obj).username);
    }

}
