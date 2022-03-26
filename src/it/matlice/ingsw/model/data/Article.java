package it.matlice.ingsw.model.data;

import java.util.Map;

public abstract class Article {

    public abstract User getOwner();
    public abstract LeafCategory getCategory();
    public abstract Map<String, Object> getFieldsValue();

}
