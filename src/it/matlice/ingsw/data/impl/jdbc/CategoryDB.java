package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "categories")
public class CategoryDB {

    @DatabaseField(generatedId = true)
    private int category_id;

    public void setFather(CategoryDB father) {
        this.father = father;
    }

    @DatabaseField(canBeNull = false, uniqueCombo = true)
    private String category_name;

    @DatabaseField(canBeNull = false, uniqueCombo = false)
    private String category_description;

    @DatabaseField(canBeNull = true, foreign = true, uniqueCombo = true)
    private CategoryDB father;

    CategoryDB(String category_name, String category_description, CategoryDB father) {
        this.category_name = category_name;
        this.category_description = category_description;
        this.father = father;
    }

    CategoryDB() {
    }

    public int getCategory_id() {
        return this.category_id;
    }

    public String getCategory_description() {
        return this.category_description;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public CategoryDB getFather() {
        return this.father;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        CategoryDB that = (CategoryDB) o;
        return this.category_id == that.category_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.category_id);
    }
}
