package it.matlice.ingsw.view;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;

import java.util.Calendar;

public interface InfoFactory {

    IMessage getProposedExchange(Calendar date, Settings.Day day, Interval.Time time);

}
