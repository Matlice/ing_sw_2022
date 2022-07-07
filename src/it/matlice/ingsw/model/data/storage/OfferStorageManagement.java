package it.matlice.ingsw.model.data.storage;


import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.data.storage.factories.OfferFactory;
import it.matlice.ingsw.model.data.storage.repositories.OfferRepository;
import it.matlice.ingsw.model.exceptions.DBException;
import it.matlice.ingsw.model.exceptions.RequiredFieldConstrainException;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * rappresenta una classe che si occuperà di istanziare implementazioni di articoli,
 * con i campi compilati opportunamente e associati al proprietario
 */
public interface OfferStorageManagement extends OfferFactory, OfferRepository {

    /**
     * Imposta lo stato di un'offerta
     * @param offer istanza dell'offerta da creare
     * @param status stato dell'offerta
     * @throws DBException .
     */
    void setOfferStatus(Offer offer, Offer.OfferStatus status) throws DBException;

    /**
     * Permette di accoppiare due offerte, la prima è dell'utente che propone lo scambio
     * @param offerToTrade offerta accoppiata
     * @param offerToAccept offerta selezionata
     * @throws DBException
     */
    void linkOffersInTradeOffer(Offer offerToTrade, Offer offerToAccept) throws DBException;

    void acceptTradeOffer(Offer offer, MessageStorageManagement mf, String location, Calendar date) throws DBException;

    void updateTime(Offer offer) throws DBException;

    void closeTradeOffer(Message m) throws DBException;

    void checkForDueDate() throws DBException;
}
