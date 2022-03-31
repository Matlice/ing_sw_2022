package it.matlice.ingsw.model.data.factories;

import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.User;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface MessageFactory {
    Message send(Offer offer, String location, Calendar date) throws SQLException;
    Message answer(Message msg, Offer offer, String location, Date date) throws SQLException;
     List<Message> getUserMessages(User u) throws SQLException;
}
