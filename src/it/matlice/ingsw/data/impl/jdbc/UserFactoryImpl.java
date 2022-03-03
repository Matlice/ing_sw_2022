package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.User;
import it.matlice.ingsw.data.UserFactory;
import it.matlice.ingsw.data.impl.jdbc.types.ConfiguratorUserImpl;
import it.matlice.ingsw.data.impl.jdbc.types.UserImpl;

import java.sql.SQLException;
import java.util.List;

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

    private User makeUser(UserDB udb) {
        if (udb.getType().equals(User.UserTypes.CONFIGURATOR.getTypeRepresentation()))
            return new ConfiguratorUserImpl(udb);
        return null;
    }

    public User getUser(String username) throws SQLException {
        var udb = this.userDAO.queryForId(username);
        var u = this.makeUser(udb);
        if (u == null)
            throw new RuntimeException("no user type found");
        return u;
    }

    public User createUser(String username, User.UserTypes userType) throws SQLException {
        if (userType == User.UserTypes.CONFIGURATOR) {
            var ref = new ConfiguratorUserImpl(username);
            this.userDAO.create(ref.getDbData());
            return ref;
        }
        throw new RuntimeException("no user type found");
    }

    public User saveUser(User u) throws SQLException {
        assert u instanceof UserImpl;
        this.userDAO.update(((UserImpl) u).getDbData());
        return u;
    }

    public List<User> getUsers() throws Exception {
        return this.userDAO.queryForAll().stream()
                .map(e -> this.makeUser(e))
                .filter(e -> e != null)
                .toList();
    }

}
