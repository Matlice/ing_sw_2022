package it.matlice.ingsw.controller;

public enum WarningType {
    NEED_CHANGE_PASSWORD, // "Cambia le credenziali di accesso"
    SUCCESSFUL_REGISTRATION, //"Utente registrato con successo."
    NO_OFFERS_IN_EXCHANGE, // "Non hai offerte disponibili allo scambio"
    SUCCESSFUL_OFFER_PROPOSAL, // "Proposta di scambio confermata"
    NO_ACCEPTABLE_OFFERS, // "Non ci sono proposte di scambio accettabili al momento"
    NO_MESSAGES_TO_REPLY, // "Non hai messaggi a cui rispondere"
    SUCCESSFUL_ACCEPT_OFFER, // "Scambio accettato con successo"
    NO_RETRACTABLE_OFFER, //"Non ci sono offerte ritirabili"
    NO_LEAF_CATEGORIES_TO_SHOW, // "Non sono presenti categorie di cui mostrare le offerte"
    NO_HIERARCHIES, // "Nessuna gerarchia trovata"
    LOADING_CONFIG, // Importando la configurazione...
    CITY_NOT_OVERWRITABLE_SUCCESS, // "La città di scambio non può essere sovrascritta, le altre configurazioni sono state correttamente importate"
    CITY_NOT_OVERWRITABLE, // "Non è possibile modificare la piazza di scambio"
    IMPORTING_HIERARCHIES, // "Importando le gerarchie..."
    NO_EMPTY_NAME, // "Inserire un nome non vuoto"
    OFFER_CREATED, // "L'articolo è stato salvato con successo, è ora disponibile allo scambio"
    NO_OFFER_FOUND, // "Non sono state trovate offerte"

}
