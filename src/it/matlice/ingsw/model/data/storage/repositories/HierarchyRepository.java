package it.matlice.ingsw.model.data.storage.repositories;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.exceptions.DBException;

import java.util.List;

public interface HierarchyRepository {

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
    Hierarchy saveHierarchy(Category rootCategory) throws DBException;

    /**
     * rimuove una gerarchia dalla bd
     *
     * @param h gerarchia da rimuovere
     * @throws DBException
     */
    void deleteHierarchy(Hierarchy h) throws DBException;

}
