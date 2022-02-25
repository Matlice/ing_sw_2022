package it.matlice.ingsw.data;


import java.sql.SQLException;

public interface CategoryFactory {

    Category getCategory(int id) throws SQLException;
    Category createCategory(String nome, Category father, boolean isLeaf) throws SQLException;
}
