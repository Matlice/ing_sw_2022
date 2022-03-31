package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.impl.jdbc.db.MessageDB;

import java.util.Date;

public class MessageImpl extends Message {

    private final MessageDB dbData;
    private final Offer referenced_offer;

    public MessageImpl(MessageDB dbData, Offer referenced_offer) {
        this.dbData = dbData;
        this.referenced_offer = referenced_offer;
    }

    public MessageDB getDbData() {
        return this.dbData;
    }

    @Override
    public Offer getReferencedOffer() {
        return referenced_offer;
    }

    @Override
    public String getLocation() {
        return this.dbData.getProposedLocation();
    }

    @Override
    public Date getDate() {
        return new Date(this.dbData.getProposedDate() * 1000);
    }
}
