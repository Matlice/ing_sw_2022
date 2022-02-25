package it.matlice.ingsw.data;

import it.matlice.ingsw.data.impl.sqlite.UserTypes;

import java.sql.SQLException;

public interface UserFactory {

    User getUser(String username) throws SQLException;
    public User createUser(String username, String password, UserTypes userType) throws SQLException;

}
