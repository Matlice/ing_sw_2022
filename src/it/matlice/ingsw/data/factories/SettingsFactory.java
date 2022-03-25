package it.matlice.ingsw.data.factories;

import it.matlice.ingsw.data.Interval;
import it.matlice.ingsw.data.Settings;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public interface SettingsFactory {

    Settings readSettings() throws Exception;
    Settings makeSettings(String city, int due, @NotNull List<String> locations, @NotNull List<Settings.Day> days, @NotNull List<Interval> intervals) throws Exception;

}
