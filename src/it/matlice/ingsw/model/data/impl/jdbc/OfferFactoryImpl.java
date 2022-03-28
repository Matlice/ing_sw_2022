package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.data.factories.OfferFactory;
import it.matlice.ingsw.model.data.impl.jdbc.db.OfferDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.OfferFieldDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.CategoryFieldDB;
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

    public OfferFactoryImpl() throws SQLException {
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
    }

    private Where<CategoryFieldDB, Integer> fieldQuery(Category category) throws SQLException {
        var query = categoryFieldDAO.queryBuilder().where().eq("category_id", ((LeafCategoryImpl) category).getDbData());
        NodeCategoryImpl curcat = (NodeCategoryImpl) category.getFather();
        while(curcat != null){
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
        for(var field: category.fullEntrySet()){

            var db_field = categoryFieldDAO.query(
                    fieldQuery(category).and().eq("fieldName",field.getKey()).prepare()
            );

            if(db_field.size() == 0)
                throw new RuntimeException("field not saved");

            if(field.getValue().required() && !field_values.containsKey(field.getKey()))
                throw new RequiredFieldConstrainException(field.getKey(), category);

            if(field_values.containsKey(field.getKey()))
                values.put(db_field.get(0), field.getValue().type().serialize(field_values.get(field.getKey())));

        }

        var offer = new OfferDB(name, ((UserImpl) owner).getDbData(), ((LeafCategoryImpl) category).getDbData(), Offer.OfferStatus.OPEN);
        offerDAO.create(offer);
        for(var field: values.entrySet()){
            offerFieldDAO.create(new OfferFieldDB(field.getKey(), offer, field.getValue()));
        }

        return new OfferImpl(offer, category, owner);
    }

    private LeafCategory findCategory(List<Hierarchy> hierarchyList, int id){
        LeafCategory c;
        for (Hierarchy e: hierarchyList) {
            c = findCategory(e.getRootCategory(), id);
            if(c != null) return c;
        }
        throw new RuntimeException("Category not found but should be.");
    }

    private LeafCategory findCategory(Category cat, int id){
        assert cat instanceof CategoryImpl;
        if(((CategoryImpl) cat).getDbData().getCategoryId() == id){
            assert cat instanceof LeafCategoryImpl;
            return (LeafCategory) cat;
        }

        if(cat instanceof NodeCategoryImpl){
            for (var child : ((NodeCategoryImpl) cat).getChildren()) {
                var r = findCategory(child, id);
                if(r != null) return r;
            }
        }
        return null;
    }

    @Override
    public List<Offer> getUserOffers(User owner) throws SQLException {
        assert owner instanceof UserImpl;

        var offer_db = offerDAO.queryForEq("owner_id",  ((UserImpl) owner).getDbData());

        var offers = new LinkedList<Offer>();
        for(var a: offer_db){
            var fields = offerFieldDAO.queryForEq("offer_ref_id", a.getId());
            var art = new OfferImpl(a, findCategory(new HierarchyFactoryImpl().getHierarchies(), a.getCategory().getCategoryId()), owner);
            fields.forEach(e -> {
                if(art.getCategory().containsKey(e.getRef().getFieldName()))
                    art.put(e.getRef().getFieldName(), art.getCategory().get(e.getRef().getFieldName()).type().deserialize(e.getValue())); //java é un po' bruttino eh
            });
            offers.add(art);
        }
        return offers;
    }

    @Override
    public List<Offer> getCategoryOffers(LeafCategory cat) throws SQLException{
        assert cat instanceof LeafCategoryImpl;

        var offer_db = offerDAO.queryForEq("category_id", ((LeafCategoryImpl) cat).getDbData().getCategoryId());

        var offers = new LinkedList<Offer>();
        for(var a: offer_db){
            var fields = offerFieldDAO.queryForEq("offer_ref_id", a.getId());
            try {
                var art = new OfferImpl(a, cat, new UserFactoryImpl().getUser(a.getOwner().getUsername()));
                fields.forEach(e -> {
                    if(art.getCategory().containsKey(e.getRef().getFieldName()))
                        art.put(e.getRef().getFieldName(), art.getCategory().get(e.getRef().getFieldName()).type().deserialize(e.getValue())); //java é un po' bruttino eh
                });
                offers.add(art);
            } catch (InvalidUserException e) {/*impossibile*/}
        }
        return offers;
    }

    @Override
    public void setOfferStatus(Offer offer, Offer.OfferStatus status) throws SQLException {
        assert offer instanceof OfferImpl;
        ((OfferImpl) offer).getDbData().setStatus(status);
        this.offerDAO.update(((OfferImpl) offer).getDbData());
    }

}
