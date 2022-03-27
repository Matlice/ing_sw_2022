package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "articles")
public class ArticleDB {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private UserDB owner;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private CategoryDB category;

    ArticleDB() {
    }

    public ArticleDB(String name, UserDB owner, CategoryDB category) {
        this.name = name;
        this.owner = owner;
        this.category = category;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    public UserDB getOwner() {
        return this.owner;
    }
    public CategoryDB getCategory() {
        return this.category;
    }
}
