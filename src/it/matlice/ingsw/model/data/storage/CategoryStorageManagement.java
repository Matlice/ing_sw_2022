package it.matlice.ingsw.model.data.storage;

import it.matlice.ingsw.model.data.storage.factories.CategoryFactory;
import it.matlice.ingsw.model.data.storage.repositories.CategoryRepository;

/**
 * rappresenta una classe che si occuperà di istanziare implementazioni di categorie,
 * correttamente identificate da NodeCategory o LeafCategory, complete di struttura di (eventuali) figli
 */
public interface CategoryStorageManagement extends CategoryFactory, CategoryRepository { }
