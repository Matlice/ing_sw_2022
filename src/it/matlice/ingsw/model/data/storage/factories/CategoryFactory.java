package it.matlice.ingsw.model.data.storage.factories;

import it.matlice.ingsw.model.data.Category;

public interface CategoryFactory {

    /**
     * crea e salva una nuova categoria
     *
     * @param nome   nome della categoria
     * @param father categoria padre (null se si vuole creare una root category
     * @param isLeaf indica se la categoria creata potrà avere figli o se è eldiana nel finale alternativo di aot
     * @return la categoria creata
     */
    Category createCategory(String nome, String description, Category father, boolean isLeaf);

}
