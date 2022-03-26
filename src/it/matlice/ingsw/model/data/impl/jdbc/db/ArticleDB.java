package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "articles")
public class ArticleDB {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(foreign = true, canBeNull = false)
    private UserDB owner;

    ArticleDB() {
    }

    public ArticleDB(UserDB owner) {
        this.owner = owner;
    }

    public Integer getId() {
        return this.id;
    }

    public UserDB getOwner() {
        return this.owner;
    }
}
