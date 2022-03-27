package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "offer_fields")
public class OfferFieldDB {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private CategoryFieldDB ref;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private OfferDB offer_ref;

    @DatabaseField(canBeNull = false)
    private String value;

    OfferFieldDB() {
    }

    public String getValue() {
        return this.value;
    }

    public OfferFieldDB(CategoryFieldDB ref, OfferDB aref, String value) {
        this.ref = ref;
        this.value = value;
        this.offer_ref = aref;
    }

    public Integer getId() {
        return this.id;
    }

    public CategoryFieldDB getRef() {
        return this.ref;
    }
}
