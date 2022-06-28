package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;
import it.matlice.ingsw.view.IMessage;
import it.matlice.ingsw.view.InfoFactory;
import it.matlice.ingsw.view.stream.info.StreamProposedExchange;

import java.util.Calendar;

public class StreamInfoFactory implements InfoFactory {

    @Override
    public IMessage getProposedExchange(Calendar date, Settings.Day day, Interval.Time time) {
        return new StreamProposedExchange(date, day, time);
    }
}
