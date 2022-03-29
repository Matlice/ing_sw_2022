package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.impl.jdbc.db.OfferDB;

public class OfferImpl extends Offer {

    private OfferDB dbData;

    private LeafCategory category;
    private User owner;
    private Offer linked_offer;

    public OfferDB getDbData() {
        return this.dbData;
    }

    public OfferImpl(OfferDB dbData, LeafCategory category, User owner, Offer linked_offer) {
        this.dbData = dbData;
        this.category = category;
        this.owner = owner;
        this.linked_offer = linked_offer;
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

    @Override
    public Offer getLinkedOffer() {
        return this.linked_offer;
    }

    @Override
    public Long getProposedTime() {
        return this.dbData.getProposedTime();
    }

    public void setLinkedOffer(OfferImpl linked) {
        this.linked_offer = linked;
        this.dbData.setLinkedOffer(linked.dbData);
    }

}
