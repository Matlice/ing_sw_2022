package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.factories.HierarchyFactory;
import it.matlice.ingsw.model.data.impl.jdbc.db.HierarchyDB;
import it.matlice.ingsw.model.data.impl.jdbc.types.CategoryImpl;
import it.matlice.ingsw.model.data.impl.jdbc.types.HierarchyImpl;
import it.matlice.ingsw.model.exceptions.DBException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe che si occupa di istanziare elementi di tipo Hierarchy
 * una volta caricati da una base di dati Jdbc
 */
public class HierarchyFactoryImpl implements HierarchyFactory {
    private final ConnectionSource connectionSource;
    private final Dao<HierarchyDB, Integer> hierarchyDAO;

    public HierarchyFactoryImpl() throws DBException {
        this.connectionSource = JdbcConnection.getInstance().getConnectionSource();
        try {
            this.hierarchyDAO = DaoManager.createDao(this.connectionSource, HierarchyDB.class);
            if (!this.hierarchyDAO.isTableExists()) {
                TableUtils.createTable(this.connectionSource, HierarchyDB.class);
            }
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    @Override
    public List<Hierarchy> getHierarchies() throws DBException {
        try {
            var category_factory = new CategoryFactoryImpl();
            return this.hierarchyDAO.queryForAll().stream().map(e -> {
                try {
                    var root = category_factory.getCategory(e.getRoot().getCategoryId());
                    return new HierarchyImpl(e, root);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    @Override
    public Hierarchy createHierarchy(Category rootCategory) throws DBException {
        assert rootCategory instanceof CategoryImpl;
        try {
            var ref = new HierarchyDB(((CategoryImpl) rootCategory).getDbData());
            this.hierarchyDAO.create(ref);
            return new HierarchyImpl(ref, rootCategory);
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    @Override
    public void deleteHierarchy(Hierarchy h) throws DBException {
        assert h instanceof HierarchyImpl;
        try {
            this.hierarchyDAO.delete(((HierarchyImpl) h).getDbData());
        } catch (SQLException e) {
            throw new DBException();
        }
    }
}
