package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.NodeCategory;
import it.matlice.ingsw.model.data.impl.jdbc.db.CategoryDB;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

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
    public List<LeafCategory> getChildLeafs() {
        // richiama getChildLeafs() sulle categorie figlie ricorsivamente
        // il caso base è in LeafCategoryImpl, in cui ritorna una lista con se stessa
        return Arrays
                .stream(this.getChildren())
                .map(Category::getChildLeafs)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public boolean isValidChildCategoryName(@NotNull String name) {
        return !name.equals(this.getName()) &&
                Arrays.stream(this.getChildren())
                        .map((e) -> e.isValidChildCategoryName(name))
                        .reduce(true, (a,b) -> a && b);
    }

    @Override
    public Category addChild(@NotNull Category child) {
        assert child instanceof CategoryImpl;
        ((CategoryImpl) child).getDbData().setFather(this.getDbData());
        return super.addChild(child);
    }

    @Override
    public Category removeChild(@NotNull Category child) {
        assert child instanceof CategoryImpl;
        ((CategoryImpl) child).getDbData().setFather(null);
        return super.removeChild(child);
    }
}
