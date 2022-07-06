package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.factories.MessageFactory;
import it.matlice.ingsw.model.data.factories.OfferFactory;
import it.matlice.ingsw.model.data.factories.SettingsFactory;
import it.matlice.ingsw.model.data.impl.jdbc.db.MessageDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.OfferDB;
import it.matlice.ingsw.model.data.impl.jdbc.types.MessageImpl;
import it.matlice.ingsw.model.data.impl.jdbc.types.OfferImpl;
import it.matlice.ingsw.model.data.impl.jdbc.types.UserImpl;
import it.matlice.ingsw.model.exceptions.DBException;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageFactoryImpl implements MessageFactory {
    private final OfferFactoryImpl offers_factory;
    private final Dao<MessageDB, Integer> messageDAO;
    private final Dao<OfferDB, Integer> offerDAO;

    public MessageFactoryImpl(OfferFactoryImpl offer, JdbcConnection connection) throws DBException {
        ConnectionSource connectionSource = connection.getConnectionSource();
        this.offers_factory = offer;
        try {
            this.messageDAO = DaoManager.createDao(connectionSource, MessageDB.class);
            this.offerDAO = DaoManager.createDao(connectionSource, OfferDB.class);
            if (!this.messageDAO.isTableExists())
                TableUtils.createTable(connectionSource, MessageDB.class);
            if (!this.offerDAO.isTableExists())
                TableUtils.createTable(connectionSource, OfferDB.class);
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    @Override
    public Message send(Offer offer, String location, Calendar date) throws DBException {
        return this.createMessage(offer, location, date.getTimeInMillis() / 1000L, null);
    }
    @Override
    public Message answer(Message msg, Offer offer, String location, Calendar date) throws DBException {
        assert msg instanceof MessageImpl;
        return this.createMessage(offer, location, date.getTimeInMillis() / 1000L, (MessageImpl) msg);
    }
    @Override
    public List<Message> getUserMessages(User u) throws DBException {
        assert u instanceof UserImpl;
        List<MessageDB> messages;
        try {
            messages = this.messageDAO.query(
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
        } catch (SQLException e) {
            throw new DBException();
        }
        return messages.stream().map(e -> (Message) new MessageImpl(e, this.offers_factory.instantiateOffer(e.getRelative_offer(), null))).toList();
    }

    private MessageImpl createMessage(Offer offer, String location, Long date, MessageImpl answer_to) throws DBException {
        assert offer instanceof OfferImpl;
        var msg = new MessageDB(date, location, ((OfferImpl) offer).getDbData());
        try {
            this.messageDAO.create(msg);
            if (answer_to != null) {
                answer_to.getDbData().setAnswer(msg);
                this.messageDAO.update(answer_to.getDbData());
            }
        } catch (SQLException e) {
            throw new DBException();
        }
        return new MessageImpl(msg, offer);
    }

}
