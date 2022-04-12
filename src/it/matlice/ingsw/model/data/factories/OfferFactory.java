package it.matlice.ingsw.model.data.factories;


import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.exceptions.RequiredFieldConstrainException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Calendar;
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
    List<Offer> getOffers(User owner) throws SQLException;

    /**
     * Imposta lo stato di un'offerta
     * @param offer istanza dell'offerta da creare
     * @param status stato dell'offerta
     * @throws SQLException .
     */
    void setOfferStatus(Offer offer, Offer.OfferStatus status) throws SQLException;

    /**
     * Ottiene la lista di offerte associate a una categoria.
     * Notare che l'istanza di categoria deve essere stata precedentemente creata e deve fare parte dell'albero delle categorie
     * per far si che vengano rilevati tutti i campi delle categorie padre sopra di essa.
     * @param cat categoria
     * @return offerte associate alla categoria
     * @throws SQLException .
     */
    List<Offer> getOffers(LeafCategory cat) throws SQLException;

    List<Offer> getSelectedOffers(User owner) throws SQLException;

    /**
     * Permette di accoppiare due offerte, la prima è dell'utente che propone lo scambio
     * @param offerToTrade offerta accoppiata
     * @param offerToAccept offerta selezionata
     * @throws SQLException
     */
    void createTradeOffer(Offer offerToTrade, Offer offerToAccept) throws SQLException;

    void acceptTradeOffer(Offer offer, MessageFactory mf, String location, Calendar date) throws SQLException;

    void updateTime(Offer offer) throws SQLException;

    void closeTradeOffer(Message m) throws SQLException;

    void checkForDueDate() throws SQLException;
}
