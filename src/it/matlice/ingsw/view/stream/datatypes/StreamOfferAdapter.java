package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;

import java.util.Calendar;

public class StreamOfferAdapter implements StreamDataType {

    private Offer offer;
    public StreamOfferAdapter(Offer o) {
        this.offer = o;
    }

    private String getOfferMessage(Offer o){
        StringBuilder sb = new StringBuilder();
        sb.append(o.getName()).append(" (").append(o.getCategory().fullToString()).append(") [").append(o.getStatus().getName()).append(" di ").append(o.getOwner().getUsername()).append("]\n");
        for(var k: o.entrySet()) {
            sb.append("\t").append(k.getKey()).append(" = ").append(k.getValue().toString()).append("\n");
        }
        sb.setLength(sb.length()-1);
        return sb.toString(); //todo print offerta di do
    }

    @Override
    public String getStreamRepresentation() {
        return this.getOfferMessage(this.offer) + "\nper\n" + this.getOfferMessage(this.offer.getLinkedOffer());
    }
}
