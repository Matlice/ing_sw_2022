package it.matlice.ingsw.model.data;


import java.sql.SQLException;

/**
 * rappresenta una classe che si occuperà di istanziare implementazioni di categorie,
 * correttamente identificate da NodeCategory o LeafCategory, complete di struttura di (eventuali) figli
 */
public interface CategoryFactory {

    /**
     * Ottiene una categoria tramite id.
     * notare che questo metodo non dovrebbe essere usato direttamente dal controller, ma deve essere utilizzato per
     * ottenere la categoria a partire da una gerarchia.
     * la gestione dell'id è lasciata all'implementazione e non deve incidere nello sviluppo del controller.
     *
     * @param id numero incrementale identificativo univoco della categoria
     * @return una categoria se esiste
     * @throws SQLException
     */
    Category getCategory(int id) throws SQLException;

    /**
     * crea e salva una nuova categoria
     *
     * @param nome   nome della categoria
     * @param father categoria padre (null se si vuole creare una root category
     * @param isLeaf indica se la categoria creata potrà avere figli o se è eldiana nel finale alternativo di aot
     * @return la categoria creata
     */
    Category createCategory(String nome, String description, Category father, boolean isLeaf);

    /**
     * salva la categoria nel database aggiornandola, inoltre salva i campi se non esistono.
     * Si noti che non è necessario cancellare i campi rimossi dato che da specifica, le categorie sono immutabili.
     * È necessario quindi solo aggiungere i nuovi campi al momento della creazione.
     *
     * @param category categoria da salvare
     * @return la categoria aggiornata
     * @throws SQLException
     */
    Category saveCategory(Category category) throws SQLException;
}
