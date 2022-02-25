package it.matlice.ingsw.data.impl.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class CategoryDB {

    @DatabaseField(generatedId = true)
    private int category_id;

    @DatabaseField(canBeNull = false)
    private String category_name;

    @DatabaseField(canBeNull = true, foreign = true)
    private CategoryDB father;

    CategoryDB(String category_name, CategoryDB father) {
        this.category_name = category_name;
        this.father = father;
    }

    CategoryDB() {
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public CategoryDB getFather() {
        return father;
    }
}
