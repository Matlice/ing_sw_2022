package it.matlice.ingsw.data.impl.jdbc.types;

import it.matlice.ingsw.data.NodeCategory;
import it.matlice.ingsw.data.impl.jdbc.CategoryDB;

public class NodeCategoryImpl extends NodeCategory implements CategoryImpl {
    private final CategoryDB dbData;

    public NodeCategoryImpl(CategoryDB from) {
        this.dbData = from;
    }

    public CategoryDB getDbData() {
        return this.dbData;
    }

    @Override
    public String getName() {
        return this.dbData.getCategory_name();
    }
}
