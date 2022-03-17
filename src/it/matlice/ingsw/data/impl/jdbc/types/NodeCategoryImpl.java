package it.matlice.ingsw.data.impl.jdbc.types;

import it.matlice.ingsw.data.Category;
import it.matlice.ingsw.data.NodeCategory;
import it.matlice.ingsw.data.impl.jdbc.CategoryDB;

import java.util.Arrays;

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
        return this.dbData.getCategoryName();
    }

    @Override
    public String getDescription() {
        return this.dbData.getCategoryDescription();
    }

    @Override
    public boolean isValidChildCategoryName(String name) {
        return !name.equals(this.getName()) &&
                Arrays.stream(this.getChildren())
                        .map((e) -> e.isValidChildCategoryName(name))
                        .reduce(true, (a,b) -> a && b);
    }

    @Override
    public Category addChild(Category child) {
        assert child instanceof CategoryImpl;
        ((CategoryImpl) child).getDbData().setFather(this.getDbData());
        return super.addChild(child);
    }

    @Override
    public Category removeChild(Category child) {
        assert child instanceof CategoryImpl;
        ((CategoryImpl) child).getDbData().setFather(null);
        return super.removeChild(child);
    }
}
