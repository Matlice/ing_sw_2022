package it.matlice.ingsw.data;

import java.sql.SQLException;

public interface HierarchyFactory {
    Hierarchy[] getHierarchies() throws SQLException;
    Hierarchy createHierarchy(Category rootCategory) throws SQLException;
    void deleteHierarchy(Hierarchy h) throws SQLException;
}
