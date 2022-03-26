package it.matlice.ingsw.model.data;

import java.util.HashMap;
import java.util.Map;

public abstract class Article extends HashMap<String, Object> {

    public abstract User getOwner();
    public abstract LeafCategory getCategory();
}
