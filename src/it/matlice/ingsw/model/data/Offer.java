package it.matlice.ingsw.model.data;

import java.util.HashMap;

public abstract class Offer extends HashMap<String, Object> {

    public abstract String getName();
    public abstract User getOwner();
    public abstract OfferStatus getStatus();
    public abstract LeafCategory getCategory();
    public abstract Offer getLinkedOffer();
    public abstract Long getProposedTime();

    public static enum OfferStatus{
        OPEN("Offerta aperta"),
        RETRACTED("Offerta ritirata"),
        COUPLED("Offerta accoppiata"),
        SELECTED("Offerta selezionata"),
        EXCHANGE("Offerta in scambio"),
        CLOSED("Offerta chiusa");

        public final String name;

        OfferStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
