package it.matlice.ingsw.model.data.factories;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.exceptions.DBException;

import java.sql.SQLException;
import java.util.List;

/**
 * Rappresenta una classe che si occupa di istanziare elementi di tipo Hierarchy
 * una volta caricati da una base di dati esterna.
 */
public interface HierarchyFactory {
    /**
     * @return Ottiene la lista di getrarchie presenti nel programma
     * @throws DBException
     */
    List<Hierarchy> getHierarchies() throws DBException;

    /**
     * crea una nuova gerarchia e la salva sulla base di dati
     *
     * @param rootCategory la categoria root
     * @return la nuova gerarchia
     * @throws DBException
     */
    Hierarchy createHierarchy(Category rootCategory) throws DBException;

    /**
     * rimuove una gerarchia dalla bd
     *
     * @param h gerarchia da rimuovere
     * @throws DBException
     */
    void deleteHierarchy(Hierarchy h) throws DBException;
}
