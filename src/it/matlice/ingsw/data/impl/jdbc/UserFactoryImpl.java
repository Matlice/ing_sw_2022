package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.User;
import it.matlice.ingsw.data.UserFactory;
import it.matlice.ingsw.data.impl.jdbc.types.ConfiguratorUserImpl;
import it.matlice.ingsw.data.impl.jdbc.types.StandardUserImpl;
import it.matlice.ingsw.data.impl.jdbc.types.UserImpl;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.InvalidUserTypeException;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class UserFactoryImpl implements UserFactory {
    private final Dao<UserDB, String> userDAO;

    public UserFactoryImpl() throws SQLException {
        ConnectionSource connectionSource = JdbcConnection.getInstance().getConnectionSource();
        this.userDAO = DaoManager.createDao(connectionSource, UserDB.class);
        if (!this.userDAO.isTableExists()) {
            TableUtils.createTable(connectionSource, UserDB.class);
        }
    }

    private User makeUser(UserDB udb) {
        if (udb.getType().equals(User.UserTypes.CONFIGURATOR.getTypeRepresentation()))
            return new ConfiguratorUserImpl(udb);
        if (udb.getType().equals(User.UserTypes.STANDARD.getTypeRepresentation()))
            return new StandardUserImpl(udb);
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

    public User createUser(String username, User.UserTypes userType) throws SQLException {
        var ref = switch (userType){
            case CONFIGURATOR -> new ConfiguratorUserImpl(username);
            case STANDARD -> new StandardUserImpl(username);
        };

        this.userDAO.create(ref.getDbData());
        return ref;
    }

    public User saveUser(User u) throws SQLException {
        assert u instanceof UserImpl;
        this.userDAO.update(((UserImpl) u).getDbData());
        return u;
    }

    public List<User> getUsers() throws SQLException {
        return this.userDAO.queryForAll().stream()
                .map(this::makeUser)
                .filter(Objects::nonNull)
                .toList();
    }

}
