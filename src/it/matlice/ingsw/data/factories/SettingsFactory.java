package it.matlice.ingsw.data.factories;

import it.matlice.ingsw.data.Interval;
import it.matlice.ingsw.data.Settings;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public interface SettingsFactory {

    Settings readSettings() throws SQLException;
    Settings makeSettings(String city, int due, @NotNull List<String> locations, @NotNull List<Settings.Day> days, @NotNull List<Interval> intervals) throws SQLException;

    void setDue(Settings db, int due) throws SQLException;

    void addLocation(Settings db, String l) throws SQLException;

    void addDay(Settings db, Settings.Day d) throws SQLException;

    void addInterval(Settings db, Interval i) throws SQLException;

    void removeLocations(Settings db) throws SQLException;

    void removeDays(Settings db) throws SQLException;

    void removeIntervals(Settings db) throws SQLException;
}
