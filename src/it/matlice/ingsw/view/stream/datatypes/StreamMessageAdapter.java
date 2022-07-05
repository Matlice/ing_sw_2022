package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.Message;

import java.util.Calendar;

public class StreamMessageAdapter implements StreamDataType {

    private Message message;
    public StreamMessageAdapter(Message o) {
        this.message = o;
    }

    @Override
    public String getStreamRepresentation() {
        return String.format(
                "Da %s: Proposta di scambio di\n\t%s\n\tper\n\t%s il %s alle ore %s in %s",
                this.message.getReferencedOffer().getLinkedOffer().getOwner().getUsername(),
                this.message.getReferencedOffer().toString().replaceAll("\n", "\n\t"),
                this.message.getReferencedOffer().getLinkedOffer().toString().replaceAll("\n", "\n\t"),
                String.format("%02d", this.message.getDate().get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", this.message.getDate().get(Calendar.MONTH)+1),
                this.message.getDate().get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", this.message.getDate().get(Calendar.MINUTE)),
                this.message.getLocation()
        );
    }
}