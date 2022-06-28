package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Settings;

import java.util.StringJoiner;

public class MessageAvailableDays extends AStreamMessage {

    private final Iterable<Settings.Day> days;

    public MessageAvailableDays(StreamView view, Iterable<Settings.Day> days) {
        super(view);
        this.days = days;
    }

    @Override
    public String getMessage() {
        StringJoiner sj_day = new StringJoiner(", ");
        this.days.forEach((e) -> sj_day.add(e.getName().toLowerCase()));
        return String.format("I giorni disponibili per lo scambio sono: %s\n", sj_day);
    }
}
