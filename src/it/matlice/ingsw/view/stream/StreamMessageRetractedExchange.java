package it.matlice.ingsw.view.stream;

public class StreamMessageRetractedExchange extends AStreamMessage {

    public StreamMessageRetractedExchange(StreamView view) {
        super(view);
    }

    @Override
    public String getMessage() {
        return "Offerta ritirata con successo";
    }

}
