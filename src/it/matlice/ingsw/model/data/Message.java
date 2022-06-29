package it.matlice.ingsw.model.data;

import java.util.Calendar;

public abstract class Message {

    public abstract Offer getReferencedOffer();
    public abstract String getLocation();
    public abstract Long getTime();
    public abstract Calendar getDate();
    public abstract boolean hasReply();

}
