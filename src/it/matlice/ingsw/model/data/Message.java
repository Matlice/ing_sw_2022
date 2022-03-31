package it.matlice.ingsw.model.data;

import java.util.Date;

public abstract class Message {

    public abstract Offer getReferencedOffer();
    public abstract String getLocation();
    public abstract Date getDate();

    @Override
    public String toString() {
        return String.format(
                "[%s]: Proposta di scambio di %s per %s in data %s a %s",
                getReferencedOffer().getOwner(),
                getReferencedOffer(),
                getReferencedOffer().getLinkedOffer(),
                getDate().toString(),
                getLocation()
        );
    }
}
