package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.NodeCategory;
import it.matlice.ingsw.model.data.impl.jdbc.db.CategoryDB;
import it.matlice.ingsw.model.data.impl.jdbc.db.CategoryFieldDB;

import java.util.Map;

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
        return this.dbData.getCategoryName();
    }

    @Override
    public String getDescription() {
        return this.dbData.getCategoryDescription();
    }

    @Override
    public boolean isValidChildCategoryName(String name) {
        return !name.equals(this.getName());
    }

    @Override
    public NodeCategory convertToNode() {
        var r = new NodeCategoryImpl(this.dbData);
        var father = this.getFather();
        if (father != null)
            father.removeChild(this);
        r.putAll(this);
        if (father != null)
            father.addChild(r);
        return r;
    }
}
