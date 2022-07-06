package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;

import java.util.StringJoiner;

public class StreamMessageAvailableIntervals extends AStreamMessage {

    private final Iterable<Interval> intervals;

    public StreamMessageAvailableIntervals(StreamView view, Iterable<Interval> intervals) {
        super(view);
        this.intervals = intervals;
    }

    public static String timeToString(Interval.Time t) {
        return t.getHour() + ":" + String.format("%02d", t.getMinute());
    }

    public static String intervalToString(Interval i) {
        return timeToString(i.getStart()) + "-" + timeToString(i.getEnd());
    }

    @Override
    public String getMessage() {
        StringJoiner sj_interval = new StringJoiner(", ");
        this.intervals.forEach((e) -> sj_interval.add(intervalToString(e)));
        return String.format("Gli intervalli orari disponibili per lo scambio sono: %s\n", sj_interval);
    }
}
