package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;

import java.util.Calendar;

public class MessageStreamRetractedExchange extends AStreamMessage {

    public MessageStreamRetractedExchange(StreamView view) {
        super(view);
    }

    @Override
    public String getMessage() {
        return "Offerta ritirata con successo";
    }

}
