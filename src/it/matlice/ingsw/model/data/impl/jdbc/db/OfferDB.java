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

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private OfferDB linked_offer;

    @DatabaseField()
    private Long proposed_time;

    OfferDB() {
    }

    public OfferDB(String name, UserDB owner, CategoryDB category, Offer.OfferStatus status, OfferDB linked_offer, Long proposed_time) {
        this.name = name;
        this.owner = owner;
        this.category = category;
        this.status = status.toString();
        this.linked_offer = linked_offer;
        this.proposed_time = proposed_time;
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
    public OfferDB getLinkedOffer() {
        return this.linked_offer;
    }
    public Long getProposedTime() {
        return this.proposed_time;
    }

    public void setStatus(Offer.OfferStatus status) {
        this.status = status.toString();
    }

    public void setLinkedOffer(OfferDB dbData) {
        this.linked_offer = dbData;
    }

    public void setProposedTime(long time) {
        this.proposed_time = time;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfferDB)) return false;
        if (this.id == null) return false;
        return this.id.equals(((OfferDB) obj).id);
    }

}
