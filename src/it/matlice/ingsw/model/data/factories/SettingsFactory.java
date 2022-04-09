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

    /**
     * Permette di impostare i giorni di scadenza
     * @param db istanza di settings
     * @param due numero di giorni per la scadenza
     * @throws SQLException errore di database
     */
    void setDue(Settings db, int due) throws SQLException;

    /**
     * Permette di aggiungere un nuovo luogo per lo scambio
     * @param db istanza di settings
     * @param l luogo da aggiungere
     * @throws SQLException errore di database
     */
    void addLocation(Settings db, String l) throws SQLException;

    /**
     * Permette di aggiungere un nuovo giorno per lo scambio
     * @param db istanza di settings
     * @param d giorno da aggiungere
     * @throws SQLException errore di database
     */
    void addDay(Settings db, Settings.Day d) throws SQLException;

    /**
     * Permette di aggiungere un nuovo intervallo orario per lo scambio
     * @param db istanza di settings
     * @param i intervallo da aggiungere
     * @throws SQLException errore di database
     */
    void addInterval(Settings db, Interval i) throws SQLException;

    /**
     * Permette di rimuovere tutti i luoghi per lo scambio
     * @param db istanza di settings
     * @throws SQLException errore di database
     */
    void removeLocations(Settings db) throws SQLException;

    /**
     * Permette di rimuovere tutti i giorni per lo scambio
     * @param db istanza di settings
     * @throws SQLException errore di database
     */
    void removeDays(Settings db) throws SQLException;

    /**
     * Permette di rimuovere tutti gli intervalli per lo scambio
     * @param db istanza di settings
     * @throws SQLException errore di database
     */
    void removeIntervals(Settings db) throws SQLException;
}
