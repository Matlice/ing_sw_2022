package it.matlice.ingsw.model.data.storage.factories;

import it.matlice.ingsw.model.data.LeafCategory;
import it.matlice.ingsw.model.data.Offer;
import it.matlice.ingsw.model.data.User;
import it.matlice.ingsw.model.exceptions.DBException;
import it.matlice.ingsw.model.exceptions.RequiredFieldConstrainException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface OfferFactory {

    /**
     * Crea un'offerta per l'utente nella categoria.
     * Notare che l'istanza di categoria deve essere stata precedentemente creata e deve fare parte dell'albero delle categorie
     * per far si che vengano rilevati tutti i campi delle categorie padre sopra di essa.
     * @param name nome dell'articolo offerto
     * @param owner istanza dell'utente proprietario
     * @param category categoria dove posizionare l'offerta
     * @param field_values mappa dei valori dei campi
     * @return l'offerta istanziata
     * @throws RequiredFieldConstrainException Se un campo obbligatorio non é stato compilato
     * @throws DBException .
     */
    Offer makeOffer(@NotNull String name, User owner, LeafCategory category, Map<String, Object> field_values) throws RequiredFieldConstrainException, DBException;

}
