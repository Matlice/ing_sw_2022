package it.matlice.ingsw.data;

import java.util.List;

/**
 * Rappresenta una classe che si occupa di istanziare elementi di tipo Hierarchy
 * una volta caricati da una base di dati esterna.
 */
public interface HierarchyFactory {
    /**
     * @return Ottiene la lista di getrarchie presenti nel programma
     * @throws Exception
     */
    List<Hierarchy> getHierarchies() throws Exception;

    /**
     * crea una nuova gerarchia e la salva sulla base di dati
     *
     * @param rootCategory la categoria root
     * @return la nuova gerarchia
     * @throws Exception
     */
    Hierarchy createHierarchy(Category rootCategory) throws Exception;

    /**
     * rimuove una gerarchia dalla bd
     *
     * @param h gerarchia da rimuovere
     * @throws Exception
     */
    void deleteHierarchy(Hierarchy h) throws Exception;
}
