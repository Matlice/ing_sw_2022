package it.matlice.ingsw.data.impl.sqlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.User;
import it.matlice.ingsw.data.UserFactory;
import it.matlice.ingsw.data.impl.sqlite.types.ConfiguratorUserImpl;
import org.sqlite.*;

import java.sql.SQLException;

public class UserFactoryImpl  implements UserFactory{
    private ConnectionSource connectionSource;
    private Dao<UserDB,String> userDAO;

    public UserFactoryImpl(ConnectionSource connectionSource) throws SQLException {
        this.connectionSource = connectionSource;
        this.userDAO = DaoManager.createDao(connectionSource, UserDB.class);
        if( !this.userDAO.isTableExists()){
            TableUtils.createTable(connectionSource, UserDB.class);
        }
    }

    public User getUser(String username) throws SQLException {
        var udb = userDAO.queryForId(username);
        if(udb.getType().equals(UserTypes.CONFIGURATOR.getTypeRepresentation()))
            return new ConfiguratorUserImpl(udb);
        throw new RuntimeException("no user type found");
    }

    public User createUser(String username, String password, UserTypes userType) throws SQLException {
        if(userType == UserTypes.CONFIGURATOR){
            var ref = new ConfiguratorUserImpl(username, password);
            userDAO.create(ref.getDbData());
            return ref;
        }
        throw new RuntimeException("no user type found");
    }
}
