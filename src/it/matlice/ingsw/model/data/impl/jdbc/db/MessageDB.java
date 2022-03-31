package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "messages")
public class MessageDB {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private Long proposedDate;

    @DatabaseField(canBeNull = false)
    private String proposedLocation;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private MessageDB answer;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private OfferDB relative_offer;

    public MessageDB(Long proposedDate, String proposedLocation, OfferDB relative_offer) {
        this.proposedDate = proposedDate;
        this.proposedLocation = proposedLocation;
        this.relative_offer = relative_offer;
    }

    MessageDB(){}

    public void setAnswer(MessageDB answer) {
        this.answer = answer;
    }

    public Integer getId() {
        return this.id;
    }

    public Long getProposedDate() {
        return this.proposedDate;
    }

    public String getProposedLocation() {
        return this.proposedLocation;
    }

    public MessageDB getAnswer() {
        return this.answer;
    }

    public OfferDB getRelative_offer() {
        return this.relative_offer;
    }
}
