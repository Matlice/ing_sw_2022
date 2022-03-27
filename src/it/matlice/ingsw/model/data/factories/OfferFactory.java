package it.matlice.ingsw.model.data.factories;


import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.exceptions.RequiredFieldConstrainException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * rappresenta una classe che si occuperà di istanziare implementazioni di articoli,
 * con i campi compilati opportunamente e associati al proprietario
 */
public interface OfferFactory {

    /**
     * Crea un'offerta per l'utente nella categoria.
     * Notare che l'istanza di categoria deve essere stata precedentemente creata e deve fare parte dell'albero delle categorie
     * per far si che vengano rilevati tutti i campi delle categorie padre sopra di essa.
     * @param name nome dell'articolo offerto
     * @param owner istanza dell'utente proprietario
     * @param category categoria dove posizionare l'offerta
     * @param field_values mappa dei valori dei campi
     * @return l'offerta istanziata
     * @throws RequiredFieldConstrainException Se un campo obbligatorio non é stato compilato
     * @throws SQLException .
     */
    Offer makeOffer(@NotNull String name, User owner, LeafCategory category, Map<String, Object> field_values) throws RequiredFieldConstrainException, SQLException;

    /**
     *
     * @param owner utente
     * @return Ritorna tutte le offerte associate all'utente
     * @throws SQLException .
     */
    List<Offer> getUserOffers(User owner) throws SQLException;

    /**
     * Imposta lo stato di un'offerta
     * @param offer istanza dell'offerta da creare
     * @param status stato dell'offerta
     * @throws SQLException .
     */
    public void setOfferStatus(Offer offer, Offer.OfferStatus status) throws SQLException;

    /**
     * Ottiene la lista di offerte associate a una categoria.
     * Notare che l'istanza di categoria deve essere stata precedentemente creata e deve fare parte dell'albero delle categorie
     * per far si che vengano rilevati tutti i campi delle categorie padre sopra di essa.
     * @param cat categoria
     * @return offerte associate alla categoria
     * @throws SQLException .
     */
    public List<Offer> getCategoryOffers(LeafCategory cat) throws SQLException;
}
