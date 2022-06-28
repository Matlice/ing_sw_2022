package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;

public class MessageShowConfigParameters extends AStreamMessage {

    private final String city;
    private final Iterable<String> locations;
    private final Iterable<Settings.Day> days;
    private final Iterable<Interval> intervals;
    private final int due;

    public MessageShowConfigParameters(StreamView view,
                                       String city,
                                       Iterable<String> locations,
                                       Iterable<Settings.Day> days,
                                       Iterable<Interval> intervals,
                                       int due) {
        super(view);
        this.city = city;
        this.locations = locations;
        this.days = days;
        this.intervals = intervals;
        this.due = due;
    }

    @Override
    public String getMessage() {
         return this.hierarchy.getRootCategory().toString(); // todo togli toString() // todo AAAAAAAAAAAAAAAAAAAAAAAAAa
    }
}
