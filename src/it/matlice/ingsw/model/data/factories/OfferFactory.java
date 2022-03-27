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

    Offer makeOffer(@NotNull String name, User owner, LeafCategory category, Map<String, Object> field_values) throws RequiredFieldConstrainException, SQLException;

    List<Offer> getUserOffers(User owner) throws SQLException;

    public void setOfferStatus(Offer offer, Offer.OfferStatus status) throws SQLException;

    public List<Offer> getCategoryOffers(LeafCategory cat) throws SQLException;

}