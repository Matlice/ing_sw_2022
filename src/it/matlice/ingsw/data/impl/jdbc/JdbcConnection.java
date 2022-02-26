package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class JdbcConnection {
    private static JdbcConnection instance = null;

    private ConnectionSource cs = null;

    private JdbcConnection() {
        throw new RuntimeException("Database has not been instantiated");
    }

    private JdbcConnection(String url) throws SQLException {
        this.cs = new JdbcConnectionSource(url);
    }

    public static JdbcConnection getInstance() {
        return instance;
    }

    public static JdbcConnection startInstance(String url) throws SQLException {
        if (instance == null) instance = new JdbcConnection(url);
        return instance;
    }

    public void close() throws Exception {
        cs.close();
    }

    public ConnectionSource getConnectionSource() {
        return this.cs;
    }
}
