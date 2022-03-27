package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "locations")
public class LocationsDB {

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private SettingsDB ref;

    @DatabaseField(canBeNull = false)
    private String location;

    LocationsDB() {
        location = "";
    }

    public LocationsDB(SettingsDB ref, String location) {
        this.ref = ref;
        this.location = location;
    }

    public SettingsDB getRef() {
        return this.ref;
    }

    public void setRef(SettingsDB ref) {
        this.ref = ref;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
