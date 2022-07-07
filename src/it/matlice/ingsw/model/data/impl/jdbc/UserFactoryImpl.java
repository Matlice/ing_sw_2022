package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.storage.UserStorageManagement;
import it.matlice.ingsw.model.data.impl.jdbc.db.UserDB;
import it.matlice.ingsw.model.data.impl.jdbc.types.ConfiguratorUserImpl;
import it.matlice.ingsw.model.data.impl.jdbc.types.CustomerUserImpl;
import it.matlice.ingsw.model.data.impl.jdbc.types.UserImpl;
import it.matlice.ingsw.model.exceptions.DBException;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.InvalidUserTypeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * classe in grado di istanziare User nella giusta declinazione
 * a partire da una base di dati Jdbc
 */
public class UserFactoryImpl implements UserStorageManagement {
    private final Dao<UserDB, String> userDAO;

    public UserFactoryImpl(JdbcConnection connection) throws DBException {
        ConnectionSource connectionSource = connection.getConnectionSource();
        try {
            this.userDAO = DaoManager.createDao(connectionSource, UserDB.class);
            if (!this.userDAO.isTableExists()) {
                TableUtils.createTable(connectionSource, UserDB.class);
            }
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    private @Nullable User makeUser(@NotNull UserDB udb) {
        if (udb.getType().equals(User.UserTypes.CONFIGURATOR.getTypeRepresentation()))
            return new ConfiguratorUserImpl(udb);
        else if (udb.getType().equals(User.UserTypes.CUSTOMER.getTypeRepresentation()))
            return new CustomerUserImpl(udb);
        return null;
    }

    public User getUser(String username) throws DBException, InvalidUserException {
        UserDB udb;
        try {
            udb = this.userDAO.queryForId(username);
        } catch (SQLException e) {
            throw new DBException();
        }
        var u = this.makeUser(udb);
        if (u == null)
            throw new InvalidUserException();
        return u;
    }

    public boolean doesUserExist(String username) throws DBException {
        UserDB udb;
        try {
            udb = this.userDAO.queryForId(username);
        } catch (SQLException e) {
            throw new DBException();
        }
        return !(udb == null);
    }

    public User createUser(String username, User.UserTypes userType) throws DBException, InvalidUserTypeException {
        var ref = switch (userType){
            case CONFIGURATOR -> new ConfiguratorUserImpl(username);
            case CUSTOMER -> new CustomerUserImpl(username);
            default -> throw new InvalidUserTypeException();
        };

        try {
            this.userDAO.create(ref.getDbData());
        } catch (SQLException e) {
            throw new DBException();
        }
        return ref;
    }

    public User saveUser(User u) throws DBException {
        assert u instanceof UserImpl;
        try {
            this.userDAO.update(((UserImpl) u).getDbData());
        } catch (SQLException e) {
            throw new DBException();
        }
        return u;
    }

    public List<User> getUsers() throws DBException {
        try {
            return this.userDAO.queryForAll().stream()
                    .map(this::makeUser)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (SQLException e) {
            throw new DBException();
        }
    }

}
