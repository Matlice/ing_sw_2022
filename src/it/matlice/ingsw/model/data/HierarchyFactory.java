package it.matlice.ingsw.model.data;

import java.sql.SQLException;
import java.util.List;

/**
 * Rappresenta una classe che si occupa di istanziare elementi di tipo Hierarchy
 * una volta caricati da una base di dati esterna.
 */
public interface HierarchyFactory {
    /**
     * @return Ottiene la lista di getrarchie presenti nel programma
     * @throws SQLException
     */
    List<Hierarchy> getHierarchies() throws SQLException;

    /**
     * crea una nuova gerarchia e la salva sulla base di dati
     *
     * @param rootCategory la categoria root
     * @return la nuova gerarchia
     * @throws SQLException
     */
    Hierarchy createHierarchy(Category rootCategory) throws SQLException;

    /**
     * rimuove una gerarchia dalla bd
     *
     * @param h gerarchia da rimuovere
     * @throws SQLException
     */
    void deleteHierarchy(Hierarchy h) throws SQLException;
}
