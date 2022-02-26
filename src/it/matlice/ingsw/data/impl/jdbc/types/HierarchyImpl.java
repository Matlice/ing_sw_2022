package it.matlice.ingsw.data.impl.jdbc.types;

import it.matlice.ingsw.data.Category;
import it.matlice.ingsw.data.Hierarchy;
import it.matlice.ingsw.data.impl.jdbc.HierarchyDB;

public class HierarchyImpl extends Hierarchy {
    private final HierarchyDB dbData;

    public HierarchyImpl(HierarchyDB from, Category root_category) {
        super(root_category);
        this.dbData = from;
    }

    public HierarchyDB getDbData() {
        return this.dbData;
    }
}
