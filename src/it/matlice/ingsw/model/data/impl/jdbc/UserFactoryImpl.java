package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.UserFactory;
import it.matlice.ingsw.model.data.impl.jdbc.types.ConfiguratorUserImpl;
import it.matlice.ingsw.model.data.impl.jdbc.types.UserImpl;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.InvalidUserTypeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

/**
 * classe in grado di istanziare User nella giusta declinazione
 * a partire da una base di dati Jdbc
 */
public class UserFactoryImpl implements UserFactory {
    private final ConnectionSource connectionSource;
    private final Dao<UserDB, String> userDAO;

    public UserFactoryImpl() throws SQLException {
        this.connectionSource = JdbcConnection.getInstance().getConnectionSource();
        this.userDAO = DaoManager.createDao(this.connectionSource, UserDB.class);
        if (!this.userDAO.isTableExists()) {
            TableUtils.createTable(this.connectionSource, UserDB.class);
        }
    }

    private @Nullable User makeUser(@NotNull UserDB udb) {
        if (udb.getType().equals(User.UserTypes.CONFIGURATOR.getTypeRepresentation()))
            return new ConfiguratorUserImpl(udb);
        return null;
    }

    public User getUser(String username) throws SQLException, InvalidUserException {
        var udb = this.userDAO.queryForId(username);
        var u = this.makeUser(udb);
        if (u == null)
            throw new InvalidUserException();
        return u;
    }

    public boolean doesUserExist(String username) throws SQLException {
        var udb = this.userDAO.queryForId(username);
        return !(udb == null);
    }

    public User createUser(String username, User.UserTypes userType) throws SQLException, InvalidUserTypeException {
        if (userType == User.UserTypes.CONFIGURATOR) {
            var ref = new ConfiguratorUserImpl(username);
            this.userDAO.create(ref.getDbData());
            return ref;
        }
        throw new InvalidUserTypeException();
    }

    public User saveUser(User u) throws SQLException {
        assert u instanceof UserImpl;
        this.userDAO.update(((UserImpl) u).getDbData());
        return u;
    }

    public List<User> getUsers() throws SQLException {
        return this.userDAO.queryForAll().stream()
                .map(e -> this.makeUser(e))
                .filter(e -> e != null)
                .toList();
    }

}
