package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Classe che rappresenta a database la tabella `hierarchies`
 */
@DatabaseTable(tableName = "hierarchies")
public class HierarchyDB {

    @DatabaseField(generatedId = true)
    private int hierarchy_id;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private CategoryDB root;

    HierarchyDB() {
    }

    public HierarchyDB(CategoryDB root) {
        this.root = root;
    }

    public CategoryDB getRoot() {
        return this.root;
    }

    public int getHierarchyId() {
        return this.hierarchy_id;
    }
}
