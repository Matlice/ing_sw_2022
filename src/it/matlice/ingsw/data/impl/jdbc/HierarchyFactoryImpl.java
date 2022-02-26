package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.Category;
import it.matlice.ingsw.data.Hierarchy;
import it.matlice.ingsw.data.HierarchyFactory;
import it.matlice.ingsw.data.impl.jdbc.types.CategoryImpl;
import it.matlice.ingsw.data.impl.jdbc.types.HierarchyImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class HierarchyFactoryImpl implements HierarchyFactory {
    private final ConnectionSource connectionSource;
    private final Dao<HierarchyDB, Integer> hierarchyDAO;

    public HierarchyFactoryImpl() throws SQLException {
        this.connectionSource = JdbcConnection.getInstance().getConnectionSource();
        this.hierarchyDAO = DaoManager.createDao(connectionSource, HierarchyDB.class);
        if (!this.hierarchyDAO.isTableExists()) {
            TableUtils.createTable(connectionSource, HierarchyDB.class);
        }
    }

    @Override
    public List<Hierarchy> getHierarchies() throws SQLException {
        var category_factory = new CategoryFactoryImpl();
        return hierarchyDAO.queryForAll().stream().map(e -> {
            try {
                var root = category_factory.getCategory(e.getRoot().getCategory_id());
                return new HierarchyImpl(e, root);
            } catch (SQLException ex) {
                return null;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public Hierarchy createHierarchy(Category rootCategory) throws SQLException {
        assert rootCategory instanceof CategoryImpl;
        var ref = new HierarchyDB(((CategoryImpl) rootCategory).getDbData());
        hierarchyDAO.create(ref);
        return new HierarchyImpl(ref, rootCategory);
    }

    @Override
    public void deleteHierarchy(Hierarchy h) throws SQLException {
        assert h instanceof HierarchyImpl;
        hierarchyDAO.delete(((HierarchyImpl) h).getDbData());
    }
}
