package it.matlice.ingsw.model.data.storage;

import it.matlice.ingsw.model.data.storage.factories.UserFactory;
import it.matlice.ingsw.model.data.storage.repositories.UserRepository;

/**
 * Interfaccia che rappresenta una classe in grado di istanziare User nella giusta declinazione
 * a partire da una base di dati
 */
public interface UserStorageManagement extends UserFactory, UserRepository { }
