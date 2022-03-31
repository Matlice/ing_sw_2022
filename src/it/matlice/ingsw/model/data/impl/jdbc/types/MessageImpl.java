package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.impl.jdbc.db.MessageDB;

import java.util.Date;

public class MessageImpl extends Message {

    private final MessageDB dbData;

    public MessageImpl(MessageDB dbData) {
        this.dbData = dbData;
    }

    public MessageDB getDbData() {
        return this.dbData;
    }

    @Override
    public String getLocation() {
        return this.dbData.getProposedLocation();
    }

    @Override
    public Date getDate() {
        return new Date(this.dbData.getProposedDate() * 1000);
    }
}
