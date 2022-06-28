package it.matlice.ingsw.view.stream.info;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;
import it.matlice.ingsw.view.stream.IStreamMessage;

import java.util.Calendar;

public class StreamProposedExchange implements IStreamMessage {

    private final Calendar date;
    private final Settings.Day day;
    private final Interval.Time time;

    public StreamProposedExchange(Calendar date, Settings.Day day, Interval.Time time) {
        this.date = date;
        this.day = day;
        this.time = time;
    }

    @Override
    public String getMessage() {

        return String.format("Proposto lo scambio per il giorno %s %02d/%02d alle ore %s",
                this.day.getName(),
                this.date.get(Calendar.DAY_OF_MONTH), 
                this.date.get(Calendar.MONTH) + 1,
                this.time
        );
    }

}
