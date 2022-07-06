package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;

import java.util.StringJoiner;

import static it.matlice.ingsw.view.stream.StreamUtil.intervalToString;

public class StreamMessageAvailableIntervals extends AStreamMessage {

    private final Iterable<Interval> intervals;

    public StreamMessageAvailableIntervals(StreamView view, Iterable<Interval> intervals) {
        super(view);
        this.intervals = intervals;
    }

    @Override
    public String getMessage() {
        StringJoiner sj_interval = new StringJoiner(", ");
        this.intervals.forEach((e) -> sj_interval.add(intervalToString(e)));
        return String.format("Gli intervalli orari disponibili per lo scambio sono: %s\n", sj_interval);
    }
}
