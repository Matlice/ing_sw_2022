package it.matlice.ingsw.model.data.storage;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.storage.repositories.HierarchyRepository;
import it.matlice.ingsw.model.exceptions.DBException;

import java.util.List;

/**
 * Rappresenta una classe che si occupa di istanziare elementi di tipo Hierarchy
 * una volta caricati da una base di dati esterna.
 */
public interface HierarchyStorageManagement extends HierarchyRepository { }
