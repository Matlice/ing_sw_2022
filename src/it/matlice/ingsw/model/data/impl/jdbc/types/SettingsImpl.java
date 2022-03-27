package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;
import it.matlice.ingsw.model.data.impl.jdbc.SettingsFactoryImpl;
import it.matlice.ingsw.model.data.impl.jdbc.db.DaysDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.IntervalsDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.LocationsDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.SettingsDB;

import java.sql.SQLException;
import java.util.List;

public class SettingsImpl extends Settings {
    public SettingsImpl(SettingsDB dbData, List<LocationsDB> dbLocations, List<IntervalsDB> dbIntervals, List<DaysDB> dbDays) {
        this.dbData = dbData;
        this.dbLocations = dbLocations;
        this.dbIntervals = dbIntervals;
        this.dbDays = dbDays;
    }

    private SettingsDB dbData;
    private List<LocationsDB> dbLocations;
    private List<IntervalsDB> dbIntervals;
    private List<DaysDB> dbDays;

    @Override
    public String getCity() {
        return this.dbData.getCity();
    }

    public SettingsDB getDbData() {
        return this.dbData;
    }

    @Override
    public int getDue() {
        return this.dbData.getDue();
    }

    @Override
    public List<String> getLocations() {
        return this.dbLocations.stream().map(e -> e.getLocation()).toList();
    }

    @Override
    public List<Interval> getIntervals() {
        return this.dbIntervals.stream().map(e -> e.getInterval()).toList();
    }
    @Override
    public List<Day> getDays() {
        return this.dbDays.stream().map(e -> e.getDay()).toList();
    }
}
