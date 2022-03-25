package it.matlice.ingsw.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.matlice.ingsw.data.Interval;

@DatabaseTable(tableName = "intervals")
public class IntervalsDB {

    @DatabaseField(foreign = true)
    private SettingsDB ref;

    @DatabaseField(canBeNull = false)
    private Integer interval_start;

    @DatabaseField(canBeNull = false)
    private Integer interval_end;


    IntervalsDB() {}

    public IntervalsDB(SettingsDB ref, Interval interval) {
        this.ref = ref;
        this.interval_start = interval.getStartingMinute();
        this.interval_end = interval.getEndingMinute();
    }

    public SettingsDB getRef() {
        return this.ref;
    }

    public void setRef(SettingsDB ref) {
        this.ref = ref;
    }

    public Interval getInterval() {
        return new Interval(this.interval_start, this.interval_end);
    }

    public void setInterval(Interval interval) {
        this.interval_start = interval.getStartingMinute();
        this.interval_end = interval.getEndingMinute();
    }
}
