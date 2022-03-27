package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.data.factories.ArticleFactory;
import it.matlice.ingsw.model.data.impl.jdbc.db.ArticleDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.ArticleFieldDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.CategoryDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.CategoryFieldDB;
import it.matlice.ingsw.model.data.impl.jdbc.types.*;
import it.matlice.ingsw.model.exceptions.RequiredFieldConstrainException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class ArticleFactoryImpl implements ArticleFactory {

    private final Dao<ArticleDB, Integer> articleDAO;
    private final Dao<ArticleFieldDB, Integer> articleFieldDAO;
    private final Dao<CategoryFieldDB, Integer> categoryFieldDAO;

    public ArticleFactoryImpl() throws SQLException {
        var connectionSource = JdbcConnection.getInstance().getConnectionSource();

        this.articleDAO = DaoManager.createDao(connectionSource, ArticleDB.class);
        this.articleFieldDAO = DaoManager.createDao(connectionSource, ArticleFieldDB.class);
        this.categoryFieldDAO = DaoManager.createDao(connectionSource, CategoryFieldDB.class);

        if (!this.articleDAO.isTableExists())
            TableUtils.createTable(connectionSource, ArticleDB.class);
        if (!this.articleFieldDAO.isTableExists())
            TableUtils.createTable(connectionSource, ArticleFieldDB.class);
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

    public Article makeArticle(@NotNull String name, User owner, LeafCategory category, Map<String, Object> field_values) throws RequiredFieldConstrainException, SQLException {
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

        var article = new ArticleDB(name, ((UserImpl) owner).getDbData(), ((LeafCategoryImpl) category).getDbData());
        articleDAO.create(article);
        for(var field: values.entrySet()){
            articleFieldDAO.create(new ArticleFieldDB(field.getKey(), article, field.getValue()));
        }

        return new ArticleImpl(article, category, owner);
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

        assert cat instanceof NodeCategoryImpl;
        for (var child : ((NodeCategoryImpl) cat).getChildren()) {
            var r = findCategory(child, id);
            if(r != null) return r;
        }
        return null;
    }

    public List<Article> getUserArticles(User owner, List<Hierarchy> hierarchyList) throws SQLException {
        assert owner instanceof UserImpl;

        var articles_db = articleDAO.queryForEq("owner_id",  ((UserImpl) owner).getDbData());

        var articles = new LinkedList<Article>();
        for(var a: articles_db){
            var fields = articleFieldDAO.queryForEq("article_ref_id", a.getId());
            var art = new ArticleImpl(a, findCategory(hierarchyList, a.getCategory().getCategoryId()), owner);
            fields.forEach(e -> {
                if(art.getCategory().containsKey(e.getRef().getFieldName()))
                    art.put(e.getRef().getFieldName(), art.getCategory().get(e.getRef().getFieldName()).type().deserialize(e.getValue())); //java é un po' bruttino eh
            });
            articles.add(art);
        }
        return articles; //todo test perché sono stane.getRef().getFieldName()co
    }
}
