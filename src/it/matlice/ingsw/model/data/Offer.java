package it.matlice.ingsw.model.data;

import java.util.HashMap;

public abstract class Offer extends HashMap<String, Object> {

    public abstract User getOwner();
    public abstract OfferStatus getStatus();
    public abstract LeafCategory getCategory();

    public static enum OfferStatus{
        APERTA,
        RITIRATA
    }
}
