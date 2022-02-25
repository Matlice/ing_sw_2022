package it.matlice.ingsw.data.impl.sqlite.types;

import it.matlice.ingsw.data.LeafCategory;
import it.matlice.ingsw.data.NodeCategory;
import it.matlice.ingsw.data.impl.sqlite.CategoryDB;

public class NodeCategoryImpl extends NodeCategory {
    private CategoryDB dbData;

    public CategoryDB getDbData() {
        return dbData;
    }

    public NodeCategoryImpl(CategoryDB from){
        this.dbData = from;
    }

    @Override
    public String getName() {
        return dbData.getCategory_name();
    }
}
