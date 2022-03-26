package it.matlice.ingsw.model.exceptions;

import it.matlice.ingsw.model.data.LeafCategory;

public class RequiredFieldConstrainException extends Exception{

    public String getField() {
        return this.field;
    }

    public LeafCategory getCategory() {
        return this.category;
    }

    public RequiredFieldConstrainException(String field, LeafCategory category) {
        this.field = field;
        this.category = category;
    }

    private String field;
    private LeafCategory category;


}
