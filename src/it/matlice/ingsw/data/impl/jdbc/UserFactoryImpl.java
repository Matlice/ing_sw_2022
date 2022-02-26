package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.User;
import it.matlice.ingsw.data.UserFactory;
import it.matlice.ingsw.data.impl.jdbc.types.ConfiguratorUserImpl;

import java.sql.SQLException;

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

    public User getUser(String username) throws SQLException {
        var udb = this.userDAO.queryForId(username);
        if (udb.getType().equals(User.UserTypes.CONFIGURATOR.getTypeRepresentation()))
            return new ConfiguratorUserImpl(udb);
        throw new RuntimeException("no user type found");
    }

    public User createUser(String username, User.UserTypes userType) throws SQLException {
        if (userType == User.UserTypes.CONFIGURATOR) {
            var ref = new ConfiguratorUserImpl(username);
            this.userDAO.create(ref.getDbData());
            return ref;
        }
        throw new RuntimeException("no user type found");
    }
}
