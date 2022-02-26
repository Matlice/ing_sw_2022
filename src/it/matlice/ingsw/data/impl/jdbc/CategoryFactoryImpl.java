package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.Category;
import it.matlice.ingsw.data.CategoryFactory;
import it.matlice.ingsw.data.TypeDefinition;
import it.matlice.ingsw.data.impl.jdbc.types.CategoryImpl;
import it.matlice.ingsw.data.impl.jdbc.types.LeafCategoryImpl;
import it.matlice.ingsw.data.impl.jdbc.types.NodeCategoryImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CategoryFactoryImpl implements CategoryFactory {
    private final ConnectionSource connectionSource;
    private final Dao<CategoryDB, Integer> categoryDAO;
    private final Dao<CategoryFieldDB, Integer> fieldDAO;

    public CategoryFactoryImpl() throws SQLException {
        this.connectionSource = JdbcConnection.getInstance().getConnectionSource();
        this.categoryDAO = DaoManager.createDao(connectionSource, CategoryDB.class);
        this.fieldDAO = DaoManager.createDao(connectionSource, CategoryFieldDB.class);

        if (!this.categoryDAO.isTableExists())
            TableUtils.createTable(connectionSource, CategoryDB.class);
        if (!this.fieldDAO.isTableExists())
            TableUtils.createTable(connectionSource, CategoryFieldDB.class);
    }

    private Category populateCategory(Category cat) throws SQLException {
        assert cat instanceof CategoryImpl;

        var fields = fieldDAO.query(
                fieldDAO.queryBuilder().where()
                        .eq("category_id", ((CategoryImpl) cat).getDbData().getCategory_id())
                        .prepare()
        );

        fields.forEach(e -> {
            cat.put(
                    e.getFieldName(),
                    new TypeDefinition<>(
                            TypeDefinition.TypeAssociation.valueOf(e.getType()),
                            e.isRequired()
                    )
            );
        });

        return cat;
    }

    @Override
    public Category getCategory(int id) throws SQLException {
        var map = new HashMap<CategoryDB, List<CategoryDB>>();
        var ref = categoryDAO.queryForId(id);

        var stack = new Stack<CategoryDB>();
        stack.push(ref);
        while (!stack.empty()) {
            var r = stack.pop();
            var qb = categoryDAO.queryBuilder();
            var children = categoryDAO.query(qb.where().eq("father_id", r.getCategory_id()).prepare());
            map.put(r, children);
            children.forEach(stack::push);
        }

        var obj_ref = map.get(ref).size() > 0 ? new NodeCategoryImpl(ref) : new LeafCategoryImpl(ref);
        var obj_stack = new Stack<CategoryImpl>();
        obj_stack.push(obj_ref);
        while (!obj_stack.empty()) {
            var father = obj_stack.pop();
            populateCategory((Category) father);

            map.get(father.getDbData()).forEach(e -> {
                assert father instanceof NodeCategoryImpl;
                var obj_child = map.get(e).size() > 0 ?
                        new NodeCategoryImpl(e) :
                        new LeafCategoryImpl(e);
                ((NodeCategoryImpl) father).addChild(obj_child);
                obj_stack.push(obj_child);
            });
        }

        return obj_ref;
    }

    @Override
    public Category createCategory(String nome, Category father, boolean isLeaf) throws SQLException{
        CategoryDB ref;

        if(father != null){
            assert father instanceof NodeCategoryImpl;
            ref = new CategoryDB(nome, ((NodeCategoryImpl) father).getDbData());
        } else {
            ref = new CategoryDB(nome, null);
        }

        categoryDAO.createIfNotExists(ref);

        Category ret = isLeaf ? new LeafCategoryImpl(ref) : new NodeCategoryImpl(ref);

        if (father != null)
            return ((NodeCategoryImpl) father).addChild(ret);

        return ret;
    }

    @Override
    public Category saveCategory(Category category) throws SQLException {
        assert category instanceof CategoryImpl;

        categoryDAO.update(((CategoryImpl) category).getDbData());

        for (var entry : category.entrySet()) {
            var field_name = entry.getKey();
            var field_type = entry.getValue();
            fieldDAO.createOrUpdate(
                    new CategoryFieldDB(
                            field_name,
                            ((CategoryImpl) category).getDbData(),
                            field_type.type().toString(),
                            category.isRequired(field_name)
                    )
            );
        }
        return category;
    }
}
