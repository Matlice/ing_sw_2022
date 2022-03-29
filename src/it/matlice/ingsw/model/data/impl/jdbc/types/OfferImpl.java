package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.impl.jdbc.db.OfferDB;

import java.util.Objects;

public class OfferImpl extends Offer {

    private OfferDB dbData;

    private LeafCategory category;
    private User owner;

    public OfferDB getDbData() {
        return this.dbData;
    }

    public OfferImpl(OfferDB dbData, LeafCategory category, User owner) {
        this.dbData = dbData;
        this.category = category;
        this.owner = owner;
    }

    @Override
    public String getName() {
        return this.dbData.getName();
    }

    @Override
    public User getOwner() {
        return this.owner;
    }

    @Override
    public OfferStatus getStatus() {
        return this.dbData.getStatus();
    }

    @Override
    public LeafCategory getCategory() {
        return this.category;
    }


}
