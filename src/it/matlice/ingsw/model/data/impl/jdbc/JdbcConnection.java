package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import it.matlice.ingsw.model.exceptions.DBException;

import java.sql.SQLException;

public class JdbcConnection {

    private final ConnectionSource cs;

    public JdbcConnection(String url) throws DBException {
        try {
            this.cs = new JdbcConnectionSource(url);
        } catch (SQLException e) {
            throw new DBException();
        }
    }
    
    public void close() throws Exception {
        this.cs.close();
    }

    public ConnectionSource getConnectionSource() {
        return this.cs;
    }
}
