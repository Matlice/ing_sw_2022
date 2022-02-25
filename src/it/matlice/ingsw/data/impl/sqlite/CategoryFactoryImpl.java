package it.matlice.ingsw.data.impl.sqlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.Category;
import it.matlice.ingsw.data.CategoryFactory;
import it.matlice.ingsw.data.impl.sqlite.types.LeafCategoryImpl;
import it.matlice.ingsw.data.impl.sqlite.types.NodeCategoryImpl;

import java.sql.SQLException;
import java.util.Stack;

public class CategoryFactoryImpl implements CategoryFactory {
    private ConnectionSource connectionSource;
    private Dao<CategoryDB, Integer> categoryDAO;

    public CategoryFactoryImpl(ConnectionSource connectionSource) throws SQLException {
//        connectionSource = new JdbcConnectionSource(databaseUrl);
        this.connectionSource = connectionSource;
        this.categoryDAO = DaoManager.createDao(connectionSource, CategoryDB.class);
        if( !this.categoryDAO.isTableExists()){
            TableUtils.createTable(connectionSource, CategoryDB.class);
        }
    }


    @Override
    public Category getCategory(int id) throws SQLException {
        Category ret = null;
        var stack = new Stack<CategoryDB>();
        stack.push(categoryDAO.queryForId(id));
        while(stack.empty()){
            var r = stack.pop();

            var qb = categoryDAO.queryBuilder();
            var childs = categoryDAO.query(qb.where().eq("father_id", r.getCategory_id()).prepare());

        }

        return null;
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
