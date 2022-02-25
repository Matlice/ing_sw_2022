package it.matlice.ingsw.data.impl.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "hierarchies")
public class HierarchyDB {

    @DatabaseField(generatedId = true)
    private int hierarchy_id;

    @DatabaseField(canBeNull = true, foreign = true)
    private CategoryDB root;

    HierarchyDB() {
    }

    HierarchyDB(CategoryDB root) {
        this.root = root;
    }

    public CategoryDB getRoot() {
        return root;
    }

    public int getHierarchy_id() {
        return hierarchy_id;
    }
}
