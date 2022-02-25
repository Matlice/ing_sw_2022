package it.matlice.ingsw.data.impl.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDB that = (CategoryDB) o;
        return category_id == that.category_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(category_id);
    }
}
