package it.matlice.ingsw.model.data.factories;


import it.matlice.ingsw.model.data.*;
import it.matlice.ingsw.model.exceptions.RequiredFieldConstrainException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * rappresenta una classe che si occuperà di istanziare implementazioni di articoli,
 * con i campi compilati opportunamente e associati al proprietario
 */
public interface ArticleFactory {

    Article makeArticle(@NotNull String name, User owner, LeafCategory category, Map<String, Object> field_values) throws RequiredFieldConstrainException, SQLException;

    List<Article> getUserArticles(User owner, List<Hierarchy> hierarchyList) throws SQLException;

}
