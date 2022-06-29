package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;

import java.util.Calendar;

public class StreamOfferAdapter implements StreamDataType {

    private Offer offer;
    public StreamOfferAdapter(Offer o) {
        this.offer = o;
    }

    @Override
    public String getStreamRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.offer.getName()).append(" (").append(this.offer.getCategory().fullToString()).append(") [").append(this.offer.getStatus().getName()).append(" di ").append(this.offer.getOwner().getUsername()).append("]\n");
        for(var k: this.offer.entrySet()) {
            sb.append("\t").append(k.getKey()).append(" = ").append(k.getValue().toString()).append("\n");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
}
