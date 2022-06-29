package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;
import it.matlice.ingsw.model.data.impl.jdbc.XMLImport;
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
        return new StreamMessageFirstAccessCredentials(this.view, user, password);
    }

    @Override
    public IMessage getProposedExchangeMessage(Calendar date, Settings.Day day, Interval.Time time) {
        return new StreamMessageProposedExchange(this.view, date, day, time);
    }

    @Override
    public IMessage getProposedExchangeReplyMessage(Calendar date, Settings.Day day, Interval.Time time) {
        return new StreamMessageProposedExchangeReply(this.view, date, day, time);
    }

    @Override
    public IMessage getRetractedExchangeMessage() {
        return new StreamMessageRetractedExchange(this.view);
    }

    @Override
    public IMessage getAvailableDaysMessage(Iterable<Settings.Day> days) {
        return new StreamMessageAvailableDays(this.view, days);
    }

    @Override
    public IMessage getAvailableIntervalsMessage(Iterable<Interval> intervals) {
        return new StreamMessageAvailableIntervals(this.view, intervals);
    }

    @Override
    public IMessage getConfigurationMessage(Settings settings) {
        return new StreamMessageConfiguration(this.view, settings);
    }

    @Override
    public IMessage getHierarchyInformationMessage(Hierarchy hierarchy) {
        return new StreamMessageShowHierarchy(this.view, hierarchy);
    }

    @Override
    public IMessage getMessageHierarchyImportFullfilled(XMLImport.HierarchyXML hierarchy) {
        return new StreamMessageHierarchyImportFullfilled(this.view, hierarchy);
    }
}
