package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.NodeCategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.StringJoiner;

public class StreamCategoryChainAdapter implements StreamDataType{
    private List<Category> categories;
    public StreamCategoryChainAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String getStreamRepresentation() {
        return this.categories.stream().map(Category::getName).reduce((a, b) -> a + " > " + b).orElse("");
    }
}
