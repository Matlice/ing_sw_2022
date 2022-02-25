package it.matlice.ingsw.data.impl.sqlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.Category;
import it.matlice.ingsw.data.CategoryFactory;
import it.matlice.ingsw.data.impl.sqlite.types.CategoryImpl;
import it.matlice.ingsw.data.impl.sqlite.types.LeafCategoryImpl;
import it.matlice.ingsw.data.impl.sqlite.types.NodeCategoryImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CategoryFactoryImpl implements CategoryFactory {
    private final ConnectionSource connectionSource;
    private final Dao<CategoryDB, Integer> categoryDAO;

    public CategoryFactoryImpl() throws SQLException {
//        connectionSource = new JdbcConnectionSource(databaseUrl);
        this.connectionSource = SQLiteConnection.getInstance().getConnectionSource();
        this.categoryDAO = DaoManager.createDao(connectionSource, CategoryDB.class);
        if (!this.categoryDAO.isTableExists()) {
            TableUtils.createTable(connectionSource, CategoryDB.class);
        }
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

        categoryDAO.create(ref);

        Category ret = isLeaf ? new LeafCategoryImpl(ref) : new NodeCategoryImpl(ref);

        if (father != null)
            return ((NodeCategoryImpl) father).addChild(ret);

        return ret;
    }
}
