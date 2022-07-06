package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;

import java.util.StringJoiner;

import static it.matlice.ingsw.controller.InfoType.*;
import static it.matlice.ingsw.view.stream.StreamUtil.intervalToString;

public class StreamMessageConfiguration extends AStreamMessage {

    private Settings settings;

    public StreamMessageConfiguration(StreamView view, Settings s) {
        super(view);
        this.settings = s;
    }

    @Override
    public void show() {
        this.getView().println("La piazza di scambio è " + this.settings.getCity());
        this.getView().showList(PLACES_INFO, this.settings.getLocations());
        this.getView().showList(DATES_INFO, this.settings.getDays().stream().map(Settings.Day::getName).toList());
        this.getView().showList(HOURS_INFO, this.settings.getIntervals().stream().map(StreamUtil::intervalToString).toList());
        this.getView().println("");
        this.getView().println("La scadenza è impostata a " + this.settings.getDue() + " giorni");
    }

    @Override
    public String getMessage() {
        return null;
    }
}
