package it.matlice.ingsw.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Offer extends HashMap<String, Object> {

    public abstract String getName();
    public abstract User getOwner();
    public abstract OfferStatus getStatus();
    public abstract LeafCategory getCategory();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" (").append(this.getCategory().fullToString()).append(") [").append(this.getStatus().getName()).append(" di ").append(this.getOwner().getUsername()).append("]\n");
        for(var k: this.entrySet()) {
            sb.append("\t").append(k.getKey()).append(" = ").append(k.getValue().toString()).append("\n");
        }
        return sb.toString();
    }

    public static enum OfferStatus{
        OPEN("Offerta aperta"),
        RETRACTED("Offerta ritirata");

        public final String name;

        OfferStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
