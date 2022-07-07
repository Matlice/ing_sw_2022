package it.matlice.ingsw.model.data.storage.repositories;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.exceptions.DBException;

public interface CategoryRepository {

    /**
     * Ottiene una categoria tramite id.
     * notare che questo metodo non dovrebbe essere usato direttamente dal controller, ma deve essere utilizzato per
     * ottenere la categoria a partire da una gerarchia.
     * la gestione dell'id è lasciata all'implementazione e non deve incidere nello sviluppo del controller.
     *
     * @param id numero incrementale identificativo univoco della categoria
     * @return una categoria se esiste
     * @throws DBException
     */
    Category getCategory(int id) throws DBException;



    /**
     * salva la categoria nel database aggiornandola, inoltre salva i campi se non esistono.
     * Si noti che non è necessario cancellare i campi rimossi dato che da specifica, le categorie sono immutabili.
     * È necessario quindi solo aggiungere i nuovi campi al momento della creazione.
     *
     * @param category categoria da salvare
     * @return la categoria aggiornata
     * @throws DBException
     */
    Category saveCategory(Category category) throws DBException;

}
