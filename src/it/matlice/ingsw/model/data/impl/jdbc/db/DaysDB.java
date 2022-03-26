package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.matlice.ingsw.model.data.Settings;

@DatabaseTable(tableName = "days")
public class DaysDB {

    @DatabaseField(foreign = true)
    private SettingsDB ref;

    @DatabaseField(canBeNull = false)
    private String day;

    DaysDB() {
    }

    public DaysDB(SettingsDB ref, Settings.Day day) {
        this.ref = ref;
        this.day = day.toString();
    }

    public SettingsDB getRef() {
        return this.ref;
    }

    public void setRef(SettingsDB ref) {
        this.ref = ref;
    }

    public Settings.Day getDay() {
        return Settings.Day.valueOf(this.day);
    }

    public void setLocation(Settings.Day day) {
        this.day = day.toString();
    }
}
