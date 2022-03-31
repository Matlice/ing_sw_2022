package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.factories.MessageFactory;
import it.matlice.ingsw.model.data.impl.jdbc.db.MessageDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.OfferDB;
import it.matlice.ingsw.model.data.impl.jdbc.types.MessageImpl;
import it.matlice.ingsw.model.data.impl.jdbc.types.OfferImpl;
import it.matlice.ingsw.model.data.impl.jdbc.types.UserImpl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageFactoryImpl implements MessageFactory {

    private final Dao<MessageDB, Integer> messageDAO;
    private final Dao<OfferDB, Integer> offerDAO;

    public MessageFactoryImpl() throws SQLException {
        ConnectionSource connectionSource = JdbcConnection.getInstance().getConnectionSource();
        this.messageDAO = DaoManager.createDao(connectionSource, MessageDB.class);
        this.offerDAO = DaoManager.createDao(connectionSource, OfferDB.class);
        if (!this.messageDAO.isTableExists())
            TableUtils.createTable(connectionSource, MessageDB.class);
        if (!this.offerDAO.isTableExists())
            TableUtils.createTable(connectionSource, OfferDB.class);
    }

    @Override
    public Message send(Offer offer, String location, Calendar date) throws SQLException {
        return this.createMessage(offer, location, date.getTimeInMillis() / 1000L, null);
    }
    @Override
    public Message answer(Message msg, Offer offer, String location, Date date) throws SQLException {
        //todo reset date in offer
        assert msg instanceof MessageImpl;
        return this.createMessage(offer, location, date.getTime() / 1000L, (MessageImpl) msg);
    }
    @Override
    public List<Message> getUserMessages(User u) throws SQLException {
        assert u instanceof UserImpl;
        var messages = this.messageDAO.query(
                this.messageDAO.queryBuilder()
                        .join(
                                this.offerDAO.queryBuilder()
                                        .where()
                                        .eq("owner_id", ((UserImpl) u).getDbData().getUsername())
                                        .and()
                                        .eq("status", Offer.OfferStatus.EXCHANGE)
                                        .queryBuilder())
                        .where()
                        .isNull("answer_id")
                        .prepare()
        );
        var factory = new OfferFactoryImpl(new SettingsFactoryImpl());
        return messages.stream().map(e -> (Message) new MessageImpl(e, factory.instantiateOffer(e.getRelative_offer(), null))).toList();
    }

    private MessageImpl createMessage(Offer offer, String location, Long date, MessageImpl answer_to) throws SQLException {
        assert offer instanceof OfferImpl;
        var msg = new MessageDB(date, location, ((OfferImpl) offer).getDbData());
        this.messageDAO.create(msg);
        if (answer_to != null) {
            answer_to.getDbData().setAnswer(msg);
            this.messageDAO.update(answer_to.getDbData());
        }
        return new MessageImpl(msg, offer);
    }

}
