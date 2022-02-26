package it.matlice.ingsw.data;


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
     * @throws Exception
     */
    Category getCategory(int id) throws Exception;

    /**
     * crea e salva una nuova categoria
     *
     * @param nome   nome della categoria
     * @param father categoria padre (null se si vuole creare una root category
     * @param isLeaf indica se la categoria creata potrà avere figli o se è eldiana nel finale alternativo di aot
     * @return la categoria creata
     * @throws Exception
     */
    Category createCategory(String nome, Category father, boolean isLeaf) throws Exception;

    /**
     * salva la categoria nel database aggiornandola, inoltre salva i campi se non esistono.
     * Si noti che non è necessario cancelare i campi rimossi dato che da specifica, le categorie sono immutabili.
     * È necessario quindi solo aggiungere i nuovi campi al momento della creazione.
     *
     * @param category categoria da salvare
     * @return la categoria aggiornata
     * @throws Exception
     */
    Category saveCategory(Category category) throws Exception;
}
