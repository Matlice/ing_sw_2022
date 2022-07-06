package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;

import java.util.Calendar;

import static it.matlice.ingsw.view.stream.StreamUtil.timeToString;

public class StreamMessageProposedExchange extends AStreamMessage {

    private final Calendar date;
    private final Settings.Day day;
    private final Interval.Time time;

    public StreamMessageProposedExchange(StreamView view, Calendar date, Settings.Day day, Interval.Time time) {
        super(view);
        this.date = date;
        this.day = day;
        this.time = time;
    }

    @Override
    public String getMessage() {
        return String.format("Proposto lo scambio per il giorno %s %02d/%02d alle ore %s\n",
                this.day.getName(),
                this.date.get(Calendar.DAY_OF_MONTH),
                this.date.get(Calendar.MONTH) + 1,
                timeToString(this.time)
        );
    }

}
