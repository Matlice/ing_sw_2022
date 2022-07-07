package it.matlice.ingsw.model.data.storage;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;
import it.matlice.ingsw.model.data.storage.factories.SettingsFactory;
import it.matlice.ingsw.model.data.storage.repositories.SettingsRepository;
import it.matlice.ingsw.model.exceptions.DBException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SettingsStorageManagement extends SettingsFactory, SettingsRepository {

    /**
     * Permette di impostare i giorni di scadenza
     * @param db istanza di settings
     * @param due numero di giorni per la scadenza
     * @throws DBException errore di database
     */
    void setDue(Settings db, int due) throws DBException;

    /**
     * Permette di aggiungere un nuovo luogo per lo scambio
     * @param db istanza di settings
     * @param l luogo da aggiungere
     * @throws DBException errore di database
     */
    void addLocation(Settings db, String l) throws DBException;

    /**
     * Permette di aggiungere un nuovo giorno per lo scambio
     * @param db istanza di settings
     * @param d giorno da aggiungere
     * @throws DBException errore di database
     */
    void addDay(Settings db, Settings.Day d) throws DBException;

    /**
     * Permette di aggiungere un nuovo intervallo orario per lo scambio
     * @param db istanza di settings
     * @param i intervallo da aggiungere
     * @throws DBException errore di database
     */
    void addInterval(Settings db, Interval i) throws DBException;

    /**
     * Permette di rimuovere tutti i luoghi per lo scambio
     * @param db istanza di settings
     * @throws DBException errore di database
     */
    void removeLocations(Settings db) throws DBException;

    /**
     * Permette di rimuovere tutti i giorni per lo scambio
     * @param db istanza di settings
     * @throws DBException errore di database
     */
    void removeDays(Settings db) throws DBException;

    /**
     * Permette di rimuovere tutti gli intervalli per lo scambio
     * @param db istanza di settings
     * @throws DBException errore di database
     */
    void removeIntervals(Settings db) throws DBException;
}
