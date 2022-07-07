package it.matlice.ingsw.model.data.storage.repositories;

import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.exceptions.DBException;

import java.util.List;

public interface OfferRepository {

    /**
     *
     * @param owner utente
     * @return Ritorna tutte le offerte associate all'utente
     * @throws DBException .
     */
    List<Offer> getOffers(User owner) throws DBException;

    /**
     * Ottiene la lista di offerte associate a una categoria.
     * Notare che l'istanza di categoria deve essere stata precedentemente creata e deve fare parte dell'albero delle categorie
     * per far si che vengano rilevati tutti i campi delle categorie padre sopra di essa.
     * @param cat categoria
     * @return offerte associate alla categoria
     * @throws DBException .
     */
    List<Offer> getOffers(LeafCategory cat) throws DBException;

    List<Offer> getSelectedOffers(User owner) throws DBException;

}
