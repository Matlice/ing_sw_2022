package it.matlice.ingsw.model.data.storage.repositories;

import it.matlice.ingsw.model.data.Settings;
import it.matlice.ingsw.model.exceptions.DBException;

public interface SettingsRepository {

    /**
     * Legge le impostazioni e le ritorna
     * @return impostazioni ottenute dalla base di dati. Se fossero presenti piú impostazioni, il
     * comportamento non é definito e ne verrá ritornata solo una.
     * @throws DBException .
     */
    Settings readSettings() throws DBException;

}
