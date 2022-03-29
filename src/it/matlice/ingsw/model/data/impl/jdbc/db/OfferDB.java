package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.matlice.ingsw.model.data.Offer;

@DatabaseTable(tableName = "offers")
public class OfferDB {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private UserDB owner;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private CategoryDB category;

    @DatabaseField(canBeNull = false)
    private String status;

    OfferDB() {
    }

    public OfferDB(String name, UserDB owner, CategoryDB category, Offer.OfferStatus status) {
        this.name = name;
        this.owner = owner;
        this.category = category;
        this.status = status.toString();
    }

    public Integer getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public UserDB getOwner() {
        return this.owner;
    }
    public CategoryDB getCategory() {
        return this.category;
    }
    public Offer.OfferStatus getStatus() {
        return Offer.OfferStatus.valueOf(this.status);
    }
    public void setStatus(Offer.OfferStatus status) {
        this.status = status.toString()
;    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfferDB)) return false;
        if (this.id == null) return false;
        return this.id.equals(((OfferDB) obj).id);
    }
}
