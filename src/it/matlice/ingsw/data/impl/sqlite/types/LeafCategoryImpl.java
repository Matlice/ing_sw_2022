package it.matlice.ingsw.data.impl.sqlite.types;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.AuthMethod;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.auth.password.PasswordAuthenticable;
import it.matlice.ingsw.data.Category;
import it.matlice.ingsw.data.ConfiguratorUser;
import it.matlice.ingsw.data.LeafCategory;
import it.matlice.ingsw.data.impl.sqlite.CategoryDB;
import it.matlice.ingsw.data.impl.sqlite.UserDB;
import it.matlice.ingsw.data.impl.sqlite.UserTypes;

public class LeafCategoryImpl extends LeafCategory {
    private CategoryDB dbData;

    public CategoryDB getDbData() {
        return dbData;
    }

    public LeafCategoryImpl(CategoryDB from){
        this.dbData = from;
    }

    @Override
    public String getName() {
        return dbData.getCategory_name();
    }
}
