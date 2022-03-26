package it.matlice.ingsw.model.data.impl.jdbc.types;

import it.matlice.ingsw.model.data.Article;
import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.data.impl.jdbc.db.ArticleDB;

import java.util.Map;

public class ArticleImpl extends Article {

    private ArticleDB dbData;

    private LeafCategory category;
    private User owner;

    public ArticleDB getDbData() {
        return this.dbData;
    }

    public ArticleImpl(ArticleDB dbData, LeafCategory category, User owner) {
        this.dbData = dbData;
        this.category = category;
        this.owner = owner;
    }

    @Override
    public User getOwner() {
        return this.owner;
    }

    @Override
    public LeafCategory getCategory() {
        return this.category;
    }

}
