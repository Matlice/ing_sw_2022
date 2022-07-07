package it.matlice.ingsw.model.data.storage.factories;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Settings;
import it.matlice.ingsw.model.exceptions.DBException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SettingsFactory {

    /**
     * Crea una istanza di impostazioni
     * @param city Piazza di scambio
     * @param due numero di giorni di scadenza
     * @param locations luoghi di scambio
     * @param days Giorni di scambio
     * @param intervals intervalli di scambio
     * @return l'istanza creata
     * @throws DBException .
     */
    Settings makeSettings(String city, int due, @NotNull List<String> locations, @NotNull List<Settings.Day> days, @NotNull List<Interval> intervals) throws DBException;

}
