package it.matlice.ingsw.model.data.factories;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public interface SettingsFactory {

    /**
     * Legge le impostazioni e le ritorna
     * @return impostazioni ottenute dalla base di dati. Se fossero presenti piú impostazioni, il
     * comportamento non é definito e ne verrá ritornata solo una.
     * @throws SQLException .
     */
    Settings readSettings() throws SQLException;

    /**
     * Crea una istanza di impostazioni
     * @param city Piazza di scambio
     * @param due numero di giorni di scadenza
     * @param locations luoghi di scambio
     * @param days Giorni di scambio
     * @param intervals intervalli di scambio
     * @return l'istanza creata
     * @throws SQLException .
     */
    Settings makeSettings(String city, int due, @NotNull List<String> locations, @NotNull List<Settings.Day> days, @NotNull List<Interval> intervals) throws SQLException;

    void setDue(Settings db, int due) throws SQLException;

    void addLocation(Settings db, String l) throws SQLException;

    void addDay(Settings db, Settings.Day d) throws SQLException;

    void addInterval(Settings db, Interval i) throws SQLException;

    void removeLocations(Settings db) throws SQLException;

    void removeDays(Settings db) throws SQLException;

    void removeIntervals(Settings db) throws SQLException;
}
