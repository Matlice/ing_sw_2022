package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Message;

import java.util.Calendar;

public class StreamStringAdapter implements StreamDataType{

    private final String message;
    public StreamStringAdapter(String s) {
        this.message = s;
    }

    @Override
    public String getStreamRepresentation() {
        return this.message;
    }
}
