package it.matlice.ingsw.view;

import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;

import java.util.Calendar;

public interface InfoFactory {

    IMessage getFirstAccessCredentialsMessage(String user, String password);
    IMessage getProposedExchangeMessage(Calendar date, Settings.Day day, Interval.Time time);
    IMessage getProposedExchangeReplyMessage(Calendar date, Settings.Day day, Interval.Time time);
    IMessage getRetractedExchangeMessage();
    IMessage getAvailableDaysMessage(Iterable<Settings.Day> days);
    IMessage getAvailableIntervalsMessage(Iterable<Interval> intervals);
    IMessage getShowHierarchyMessage(Hierarchy hierarchy);

}
