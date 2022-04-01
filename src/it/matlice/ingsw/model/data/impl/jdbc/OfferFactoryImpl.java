package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.data.factories.MessageFactory;
import it.matlice.ingsw.model.data.factories.OfferFactory;
import it.matlice.ingsw.model.data.factories.SettingsFactory;
import it.matlice.ingsw.model.data.impl.jdbc.db.CategoryFieldDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.OfferDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.OfferFieldDB;
import it.matlice.ingsw.model.data.impl.jdbc.types.*;
import it.matlice.ingsw.model.exceptions.InvalidUserException;
import it.matlice.ingsw.model.exceptions.RequiredFieldConstrainException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class OfferFactoryImpl implements OfferFactory {

    private final Dao<OfferDB, Integer> offerDAO;
    private final Dao<OfferFieldDB, Integer> offerFieldDAO;
    private final Dao<CategoryFieldDB, Integer> categoryFieldDAO;
    private final SettingsFactory settingsFactory;

    public OfferFactoryImpl(SettingsFactory sf) throws SQLException {
        var connectionSource = JdbcConnection.getInstance().getConnectionSource();

        this.offerDAO = DaoManager.createDao(connectionSource, OfferDB.class);
        this.offerFieldDAO = DaoManager.createDao(connectionSource, OfferFieldDB.class);
        this.categoryFieldDAO = DaoManager.createDao(connectionSource, CategoryFieldDB.class);

        if (!this.offerDAO.isTableExists())
            TableUtils.createTable(connectionSource, OfferDB.class);
        if (!this.offerFieldDAO.isTableExists())
            TableUtils.createTable(connectionSource, OfferFieldDB.class);
        if (!this.categoryFieldDAO.isTableExists())
            TableUtils.createTable(connectionSource, CategoryFieldDB.class);

        this.settingsFactory = sf;
    }

    private Where<CategoryFieldDB, Integer> fieldQuery(Category category) throws SQLException {
        var query = this.categoryFieldDAO.queryBuilder().where().eq("category_id", ((LeafCategoryImpl) category).getDbData());
        NodeCategoryImpl curcat = (NodeCategoryImpl) category.getFather();
        while (curcat != null) {
            query = query.or().eq("category_id", curcat.getDbData().getCategoryId());
            curcat = (NodeCategoryImpl) curcat.getFather();
        }
        return query;
    }

    @Override
    public Offer makeOffer(@NotNull String name, User owner, LeafCategory category, Map<String, Object> field_values) throws RequiredFieldConstrainException, SQLException {
        assert category instanceof LeafCategoryImpl;
        assert owner instanceof UserImpl;

        Map<CategoryFieldDB, String> values = new HashMap<>();
        for (var field : category.fullEntrySet()) {

            var db_field = this.categoryFieldDAO.query(
                    this.fieldQuery(category).and().eq("fieldName", field.getKey()).prepare()
            );

            if (db_field.size() == 0)
                throw new RuntimeException("field not saved");

            if (field.getValue().required() && !field_values.containsKey(field.getKey()))
                throw new RequiredFieldConstrainException(field.getKey(), category);

            if (field_values.containsKey(field.getKey()))
                values.put(db_field.get(0), field.getValue().type().serialize(field_values.get(field.getKey())));

        }

        var offer = new OfferDB(name, ((UserImpl) owner).getDbData(), ((LeafCategoryImpl) category).getDbData(), Offer.OfferStatus.OPEN, null, null);
        this.offerDAO.create(offer);
        for (var field : values.entrySet()) {
            this.offerFieldDAO.create(new OfferFieldDB(field.getKey(), offer, field.getValue()));
        }

        return new OfferImpl(offer, category, owner, null);
    }

    private LeafCategory findCategory(List<Hierarchy> hierarchyList, int id) {
        LeafCategory c;
        for (Hierarchy e : hierarchyList) {
            c = this.findCategory(e.getRootCategory(), id);
            if (c != null) return c;
        }
        throw new RuntimeException("Category not found but should be.");
    }

    private LeafCategory findCategory(Category cat, int id) {
        assert cat instanceof CategoryImpl;
        if (((CategoryImpl) cat).getDbData().getCategoryId() == id) {
            assert cat instanceof LeafCategoryImpl;
            return (LeafCategory) cat;
        }

        if (cat instanceof NodeCategoryImpl) {
            for (var child : ((NodeCategoryImpl) cat).getChildren()) {
                var r = this.findCategory(child, id);
                if (r != null) return r;
            }
        }
        return null;
    }

    @Override
    public void checkForDueDate() throws SQLException {
        var time = System.currentTimeMillis() / 1000L; // actual time
        var due_delta = this.settingsFactory.readSettings().getDue() * 24 * 60 * 60; // expiration time

        // if time > proposed_time + due_delta then the offer has expired
        // proposed_time < time - due_delta
        var due = time - due_delta;

        UpdateBuilder<OfferDB, Integer> offerUpdateBuilder = this.offerDAO.updateBuilder();
        offerUpdateBuilder.updateColumnValue("status", Offer.OfferStatus.OPEN.toString())
                .updateColumnValue("proposed_time", null)
                .updateColumnValue("linked_offer_id", null)
                .where()
                .lt("proposed_time", due)
                .and()
                .ne("status", Offer.OfferStatus.RETRACTED)
                .and()
                .ne("status", Offer.OfferStatus.CLOSED);
        PreparedUpdate<OfferDB> preparedUpdate = offerUpdateBuilder.prepare();
        this.offerDAO.update(preparedUpdate);
    }

    OfferImpl instantiateOffer(@NotNull OfferDB offerDb, OfferImpl linkedOffer) {
        try {
            List<OfferFieldDB> fields = this.offerFieldDAO.queryForEq("offer_ref_id", offerDb.getId());
            OfferImpl art = null;
            if (offerDb.getLinkedOffer() == null) {
                art = new OfferImpl(offerDb,
                        this.findCategory(new HierarchyFactoryImpl().getHierarchies(), offerDb.getCategory().getCategoryId()),
                        new UserFactoryImpl().getUser(offerDb.getOwner().getUsername()),
                        null);
            } else if (linkedOffer != null && offerDb.getLinkedOffer().getId().equals(linkedOffer.getDbData().getId())) {
                art = new OfferImpl(offerDb,
                        this.findCategory(new HierarchyFactoryImpl().getHierarchies(), offerDb.getCategory().getCategoryId()),
                        new UserFactoryImpl().getUser(offerDb.getOwner().getUsername()),
                        linkedOffer);
            } else {
                art = new OfferImpl(offerDb,
                        this.findCategory(new HierarchyFactoryImpl().getHierarchies(), offerDb.getCategory().getCategoryId()),
                        new UserFactoryImpl().getUser(offerDb.getOwner().getUsername()),
                        null);

                OfferImpl linked = this.instantiateOffer(offerDb.getLinkedOffer(), art);

                art.setLinkedOffer(linked);
            }

            for (var f : fields) {
                if (art.getCategory().containsKey(f.getRef().getFieldName()))
                    art.put(f.getRef().getFieldName(),
                            art.getCategory().get(f.getRef().getFieldName()).type().deserialize(f.getValue()));
            }
            return art;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InvalidUserException e) {/* impossible */}
        return null;
    }

    @Override
    public List<Offer> getOffers(User owner) throws SQLException {
        this.checkForDueDate();
        assert owner instanceof UserImpl;

        var offer_db = this.offerDAO.queryForEq("owner_id", ((UserImpl) owner).getDbData());

        var offers = new LinkedList<Offer>();
        for (var a : offer_db) {
            offers.add(this.instantiateOffer(a, null));
        }
        return offers;
    }

    @Override
    public List<Offer> getOffers(LeafCategory cat) throws SQLException {
        this.checkForDueDate();
        assert cat instanceof LeafCategoryImpl;

        var offer_db = this.offerDAO.queryForEq("category_id", ((LeafCategoryImpl) cat).getDbData().getCategoryId());

        var offers = new LinkedList<Offer>();
        for (var a : offer_db) {
            offers.add(this.instantiateOffer(a, null));
        }
        return offers;
    }

    @Override
    public List<Offer> getSelectedOffers(User owner) throws SQLException {
        this.checkForDueDate();
        assert owner instanceof UserImpl;

        var offer_db = this.offerDAO.query(
                this.offerDAO.queryBuilder()
                        .where()
                        .eq("owner_id", ((UserImpl) owner).getDbData())
                        .and()
                        .eq("status", Offer.OfferStatus.SELECTED.toString())
                        .prepare()
        );

        var offers = new LinkedList<Offer>();
        for (var a : offer_db) {
            offers.add(this.instantiateOffer(a, null));
        }
        return offers;
    }


    @Override
    public void setOfferStatus(Offer offer, Offer.OfferStatus status) throws SQLException {
        assert offer instanceof OfferImpl;
        ((OfferImpl) offer).getDbData().setStatus(status);
        this.offerDAO.update(((OfferImpl) offer).getDbData());
    }

    private void setOfferLinked(Offer offerToAccept, Offer offerToTrade) throws SQLException {
        assert offerToAccept instanceof OfferImpl;
        assert offerToTrade instanceof OfferImpl;
        ((OfferImpl) offerToAccept).setLinkedOffer((OfferImpl) offerToTrade);
        this.offerDAO.update(((OfferImpl) offerToAccept).getDbData());
    }

    private void setOfferProposedTime(Offer offer, long time) throws SQLException {
        assert offer instanceof OfferImpl;
        ((OfferImpl) offer).getDbData().setProposedTime(time);
        this.offerDAO.update(((OfferImpl) offer).getDbData());
    }

    @Override
    public void createTradeOffer(Offer offerToTrade, Offer offerToAccept) throws SQLException {
        this.setOfferStatus(offerToTrade, Offer.OfferStatus.COUPLED);
        this.setOfferStatus(offerToAccept, Offer.OfferStatus.SELECTED);
        this.setOfferLinked(offerToAccept, offerToTrade);
        this.setOfferLinked(offerToTrade, offerToAccept);
        var time = System.currentTimeMillis() / 1000L;
        this.setOfferProposedTime(offerToTrade, time);
        this.setOfferProposedTime(offerToAccept, time);
    }

    @Override
    public void acceptTradeOffer(Offer offer, MessageFactory mf, String location, Calendar date) throws SQLException {
        assert offer.getLinkedOffer() != null;
        assert offer.getLinkedOffer().getLinkedOffer().equals(offer);

        this.setOfferStatus(offer, Offer.OfferStatus.EXCHANGE);
        this.setOfferStatus(offer.getLinkedOffer(), Offer.OfferStatus.EXCHANGE);

        var time = System.currentTimeMillis() / 1000L;
        this.setOfferProposedTime(offer, time);
        this.setOfferProposedTime(offer.getLinkedOffer(), time);

        mf.send(offer.getLinkedOffer(), location, date);
    }

    @Override
    public void closeTradeOffer(Message m) throws SQLException {
        assert m.getReferencedOffer().getLinkedOffer() != null;
        assert m.getReferencedOffer().getLinkedOffer().getLinkedOffer().equals(m.getReferencedOffer());

        this.setOfferStatus(m.getReferencedOffer(), Offer.OfferStatus.CLOSED);
        this.setOfferStatus(m.getReferencedOffer().getLinkedOffer(), Offer.OfferStatus.CLOSED);

        var time = System.currentTimeMillis() / 1000L;
        this.setOfferProposedTime(m.getReferencedOffer(), time);
        this.setOfferProposedTime(m.getReferencedOffer().getLinkedOffer(), time);
    }
}
