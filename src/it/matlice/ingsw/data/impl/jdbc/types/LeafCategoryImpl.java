package it.matlice.ingsw.data.impl.jdbc.types;

import it.matlice.ingsw.data.LeafCategory;
import it.matlice.ingsw.data.impl.jdbc.CategoryDB;

public class LeafCategoryImpl extends LeafCategory implements CategoryImpl {
    private final CategoryDB dbData;

    public CategoryDB getDbData() {
        return dbData;
    }

    public LeafCategoryImpl(CategoryDB from) {
        this.dbData = from;
    }

    @Override
    public String getName() {
        return dbData.getCategory_name();
    }
}
