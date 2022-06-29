package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;

import java.util.StringJoiner;

public class StreamMessageConfiguration extends AStreamMessage {

    private Settings settings;

    public StreamMessageConfiguration(StreamView view, Settings s) {
        super(view);
        this.settings = s;
    }

    @Override
    public void show() {
        this.getView().println("La piazza di scambio è " + this.settings.getCity());
        this.getView().showList("I luoghi disponibili per lo scambio sono i seguenti:", this.settings.getLocations());
        this.getView().showList("I giorni disponibili per lo scambio sono i seguenti:", this.settings.getDays().stream().map(Settings.Day::getName).toList());
        this.getView().showList("Gli intervalli disponibili per lo scambio sono i seguenti:", this.settings.getIntervals().stream().map(Interval::toString).toList());
        this.getView().println("");
        this.getView().println("La scadenza è impostata a " + this.settings.getDue() + " giorni");
    }

    @Override
    public String getMessage() {
        return null;
    }
}
