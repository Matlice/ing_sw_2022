package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;
import it.matlice.ingsw.view.IMessage;
import it.matlice.ingsw.view.InfoFactory;

import java.util.Calendar;

public class StreamInfoFactory implements InfoFactory {

    private final StreamView view;

    public StreamInfoFactory(StreamView view) {
        this.view = view;
    }

    @Override
    public IMessage getFirstAccessCredentialsMessage(String user, String password) {
        return new MessageFirstAccessCredentials(this.view, user, password);
    }

    @Override
    public IMessage getProposedExchangeMessage(Calendar date, Settings.Day day, Interval.Time time) {
        return new MessageStreamProposedExchange(this.view, date, day, time);
    }

    @Override
    public IMessage getProposedExchangeReplyMessage(Calendar date, Settings.Day day, Interval.Time time) {
        return new MessageStreamProposedExchangeReply(this.view, date, day, time);
    }

    @Override
    public IMessage getRetractedExchangeMessage() {
        return new MessageStreamRetractedExchange(this.view);
    }

    @Override
    public IMessage getAvailableDaysMessage(Iterable<Settings.Day> days) {
        return new MessageAvailableDays(this.view, days);
    }

    @Override
    public IMessage getAvailableIntervalsMessage(Iterable<Interval> intervals) {
        return new MessageAvailableIntervals(this.view, intervals);
    }

    @Override
    public IMessage getShowHierarchyMessage(Hierarchy hierarchy) {
        return new MessageShowHierarchy(this.view, hierarchy);
    }
}
