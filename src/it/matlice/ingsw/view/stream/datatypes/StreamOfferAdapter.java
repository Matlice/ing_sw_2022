package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;

import java.util.Calendar;

public class StreamOfferAdapter implements StreamDataType {

    private Offer offer;
    public StreamOfferAdapter(Offer o) {
        this.offer = o;
    }

    private StringBuilder getInfoString(Offer o) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.getName()).append(" (").append(o.getCategory().fullToString()).append(") [").append(o.getStatus().getName()).append(" di ").append(o.getOwner().getUsername()).append("]\n");
        for(var k: o.entrySet()) {
            sb.append("\t").append(k.getKey()).append(" = ").append(k.getValue().toString()).append("\n");
        }
        sb.setLength(sb.length()-1);
        return sb;
    }

    private String getOfferMessage(Offer o){
        StringBuilder sb = new StringBuilder();
        sb.append("• ");
        sb.append(this.getInfoString(o));
        return sb.toString();
    }

    private String getExchangedOfferMessage(Offer o){
        var o2 = this.offer.getLinkedOffer();
        StringBuilder sb = new StringBuilder();
        sb.append("• ");
        sb.append(this.getInfoString(o));
        sb.append("\n  per\n");
        sb.append("  " + this.getInfoString(o2));
        return sb.toString();
    }

    @Override
    public String getStreamRepresentation() {
        Offer toChange = this.offer.getLinkedOffer();
        if (toChange != null) return this.getExchangedOfferMessage(this.offer);
        return this.getOfferMessage(this.offer);
    }
}
