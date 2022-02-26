package it.matlice.ingsw.data.impl.jdbc.types;

import it.matlice.ingsw.data.LeafCategory;
import it.matlice.ingsw.data.impl.jdbc.CategoryDB;

public class LeafCategoryImpl extends LeafCategory implements CategoryImpl {
    private final CategoryDB dbData;

    public LeafCategoryImpl(CategoryDB from) {
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
