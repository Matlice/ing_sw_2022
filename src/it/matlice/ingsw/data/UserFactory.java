package it.matlice.ingsw.data;

import java.sql.SQLException;

public interface UserFactory {

    User getUser(String username) throws SQLException;

    User createUser(String username, UserTypes userType) throws SQLException;

}
