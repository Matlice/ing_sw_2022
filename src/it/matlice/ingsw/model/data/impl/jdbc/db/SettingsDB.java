package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "settings")
public class SettingsDB {

    SettingsDB() {
    }

    public SettingsDB(String city, Integer due) {
        this.city = city;
        this.due = due;
    }

    public String getCity() {
        return this.city;
    }

    public Integer getDue() {
        return this.due;
    }

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String city;

    public void setDue(Integer due) {
        this.due = due;
    }

    @DatabaseField(canBeNull = false)
    private Integer due;

    public Integer getId() {
        return this.id;
    }
}
