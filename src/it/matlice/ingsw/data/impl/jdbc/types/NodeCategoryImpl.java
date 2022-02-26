package it.matlice.ingsw.data.impl.jdbc.types;

import it.matlice.ingsw.data.NodeCategory;
import it.matlice.ingsw.data.impl.jdbc.CategoryDB;

public class NodeCategoryImpl extends NodeCategory implements CategoryImpl {
    private final CategoryDB dbData;

    public CategoryDB getDbData() {
        return dbData;
    }

    public NodeCategoryImpl(CategoryDB from) {
        this.dbData = from;
    }

    @Override
    public String getName() {
        return dbData.getCategory_name();
    }
}
