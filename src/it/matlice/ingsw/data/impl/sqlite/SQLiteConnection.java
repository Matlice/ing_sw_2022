package it.matlice.ingsw.data.impl.sqlite;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class SQLiteConnection {
    private static SQLiteConnection instance = null;

    private ConnectionSource cs = null;

    private SQLiteConnection() {
        throw new RuntimeException("Database has not been instantiated");
    }

    private SQLiteConnection(String url) throws SQLException {
        this.cs = new JdbcConnectionSource(url);
    }

    public static SQLiteConnection getInstance() {
        return instance;
    }

    public static SQLiteConnection startInstance(String url) throws SQLException {
        if (instance == null) instance = new SQLiteConnection(url);
        return instance;
    }

    public void close() throws Exception {
        cs.close();
    }

    public ConnectionSource getConnectionSource() {
        return this.cs;
    }
}
