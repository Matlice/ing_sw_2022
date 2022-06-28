package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;

import java.util.StringJoiner;

public class MessageAvailableIntervals extends AStreamMessage {

    private final Iterable<Interval> intervals;

    public MessageAvailableIntervals(StreamView view, Iterable<Interval> intervals) {
        super(view);
        this.intervals = intervals;
    }

    @Override
    public String getMessage() {
        StringJoiner sj_interval = new StringJoiner(", ");
        this.intervals.forEach((e) -> sj_interval.add(e.toString()));
        return String.format("Gli intervalli orari disponibili per lo scambio sono: %s\n", sj_interval);
    }
}
