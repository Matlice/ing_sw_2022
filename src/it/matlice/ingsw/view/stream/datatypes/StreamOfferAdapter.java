package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;

import java.util.Calendar;

import static it.matlice.ingsw.view.stream.StreamUtil.offerToString;

public class StreamOfferAdapter implements StreamDataType {

    private Offer offer;
    public StreamOfferAdapter(Offer o) {
        this.offer = o;
    }

    private String getOfferMessage(Offer o){
        StringBuilder sb = new StringBuilder();
        sb.append("• ");
        sb.append(offerToString(o));
        return sb.toString();
    }

    private String getExchangedOfferMessage(Offer o){
        var o2 = this.offer.getLinkedOffer();
        StringBuilder sb = new StringBuilder();
        sb.append("• ");
        sb.append(offerToString(o));
        sb.append("\n  per\n");
        sb.append("  ").append(offerToString(o2));
        return sb.toString();
    }

    @Override
    public String getStreamRepresentation() {
        Offer toChange = this.offer.getLinkedOffer();
        if (toChange != null) return this.getExchangedOfferMessage(this.offer);
        return this.getOfferMessage(this.offer);
    }
}
