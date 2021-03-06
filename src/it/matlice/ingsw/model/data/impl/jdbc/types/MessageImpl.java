package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.impl.jdbc.db.MessageDB;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
    public Long getTime() {
        return this.dbData.getProposedDate();
    }

    @Override
    public Calendar getDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(this.dbData.getProposedDate() * 1000);
        return cal;
    }

    @Override
    public boolean hasReply() {
        return this.getDbData().getAnswer() != null;
    }
}
