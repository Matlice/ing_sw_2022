package it.matlice.ingsw.model.data;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class Message {

    public abstract Offer getReferencedOffer();
    public abstract String getLocation();
    public abstract Long getTime();
    public abstract Calendar getDate();
    public abstract boolean hasReply();

    @Override
    public String toString() {
        return String.format(
                "Da %s: Proposta di scambio di\n\t%s\n\tper\n\t%s il %s alle ore %s in %s",
                this.getReferencedOffer().getLinkedOffer().getOwner().getUsername(),
                this.getReferencedOffer().toString().replaceAll("\n", "\n\t"),
                this.getReferencedOffer().getLinkedOffer().toString().replaceAll("\n", "\n\t"),
                String.format("%02d", this.getDate().get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", this.getDate().get(Calendar.MONTH)+1),
                this.getDate().get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", this.getDate().get(Calendar.MINUTE)),
                this.getLocation()
        );
    }

}
