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
    public SettingsImpl(SettingsFactoryImpl factory, SettingsDB dbData, List<LocationsDB> dbLocations, List<IntervalsDB> dbIntervals, List<DaysDB> dbDays) {
        this.dbData = dbData;
        this.dbLocations = dbLocations;
        this.dbIntervals = dbIntervals;
        this.dbDays = dbDays;
        this.factory = factory;
    }

    private SettingsDB dbData;
    private List<LocationsDB> dbLocations;
    private List<IntervalsDB> dbIntervals;
    private List<DaysDB> dbDays;

    private SettingsFactoryImpl factory;

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
    public void setDue(int due) {
        this.dbData.setDue(due);
    }

    @Override
    public List<String> getLocations() {
        return this.dbLocations.stream().map(e -> e.getLocation()).toList();
    }

    @Override
    public void setLocations(List<String> locations) throws SQLException {
        this.factory.removeLocations(this);
        for (var l: locations) {
            this.factory.addLocation(this, l);
        }
    }

    @Override
    public void addLocation(String location) throws SQLException{
            this.factory.addLocation(this, location);
    }

    @Override
    public List<Interval> getIntervals() {
        return this.dbIntervals.stream().map(e -> e.getInterval()).toList();
    }

    @Override
    public void setIntervals(List<Interval> intervals) {
        throw new RuntimeException();
    }

    @Override
    public void addInterval(Interval interval) throws Exception {

    }

    @Override
    public List<Day> getDays() {
        return this.dbDays.stream().map(e -> e.getDay()).toList();
    }

    @Override
    public void setDays(List<Day> days) {
        throw new RuntimeException();
    }

    @Override
    public void addDay(Day day) throws Exception {

    }
}
