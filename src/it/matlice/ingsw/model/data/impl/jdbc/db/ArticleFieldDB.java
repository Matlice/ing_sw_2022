package it.matlice.ingsw.model.data.impl.jdbc.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "article_fields")
public class ArticleFieldDB {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private CategoryFieldDB ref;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private ArticleDB article_ref;

    @DatabaseField(canBeNull = false)
    private String value;

    ArticleFieldDB() {
    }

    public String getValue() {
        return this.value;
    }

    public ArticleFieldDB(CategoryFieldDB ref, ArticleDB aref, String value) {
        this.ref = ref;
        this.value = value;
        this.article_ref = aref;
    }

    public Integer getId() {
        return this.id;
    }

    public CategoryFieldDB getRef() {
        return this.ref;
    }
}
