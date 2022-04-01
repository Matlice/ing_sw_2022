package it.matlice.ingsw.model.data.impl.jdbc;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Classe che rappresenta a database la tabella `fields`
 * in cui sono salvati i campi relativi alle categorie
 */
@DatabaseTable(tableName = "fields")
public class CategoryFieldDB {

    @DatabaseField(generatedId = true)
    private int fieldId;

    @DatabaseField(uniqueCombo = true)
    private String fieldName;

    @DatabaseField(foreign = true, uniqueCombo = true)
    private CategoryDB category;

    @DatabaseField(canBeNull = false)
    private String type;
    @DatabaseField(canBeNull = false)
    private boolean required;

    CategoryFieldDB() {
    }

    public CategoryFieldDB(String fieldName, CategoryDB category, String type, boolean required) {
        this.fieldName = fieldName;
        this.category = category;
        this.type = type;
        this.required = required;
    }

    public int getFieldId() {
        return this.fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public CategoryDB getCategory() {
        return this.category;
    }

    public void setCategory(CategoryDB category) {
        this.category = category;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
