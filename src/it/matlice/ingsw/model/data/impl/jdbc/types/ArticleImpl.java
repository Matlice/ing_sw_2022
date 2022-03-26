package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Article;
import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.impl.jdbc.db.ArticleDB;

import java.util.Map;

public class ArticleImpl extends Article {

    private ArticleDB dbData;

    public ArticleDB getDbData() {
        return this.dbData;
    }

    public ArticleImpl(ArticleDB dbData) {
        this.dbData = dbData;
    }

    @Override
    public User getOwner() {
        return null;
    }

    @Override
    public LeafCategory getCategory() {
        return null;
    }

    @Override
    public Map<String, Object> getFieldsValue() {
        return null;
    }
}
