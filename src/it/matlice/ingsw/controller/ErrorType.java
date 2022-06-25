package it.matlice.ingsw.controller;

public enum ErrorType {
    SYSTEM_NOT_CONFIGURED, // "Il sistema non è ancora utilizzabile. Contattare un configuratore."
    PASSWORD_NOT_VALID, // "La password inserita non rispetta i requisiti di sicurezza"
    PASSWORD_NOT_SAME, // "Le password non corrispondono"
    USER_NO_AUTH_METHOD, //"Nessun metodo di login disponibile per " + method
    USER_DUPLICATE, // "L'utente inserito è già esistente."
    USER_NOT_EXISTING, // "Utente non esistente"
    USER_LOGIN_INVALID, // "Il login non è più valido."
    USER_LOGIN_FAILED, // "Credenziali errate"
    ERR_DEFAULT_USER_CREATION, // "Errore nella creazione dell'utente di default"
    ERR_CONFIG_USER_CREATION, // 'Impossibile creare un nuovo configuratore'
    ERR_OFFER_PROPOSAL, // "Impossibile proporre lo scambio"
    CATEGORY_SAME_NAME_ROOT, // "Categoria radice con lo stesso nome già presente"
    CATEGORY_SAME_NAME, // "Categoria già esistente nell'albero della gerarchia"
    COULD_NOT_PARSE_IMPORT_FILE, // "Impossibile decifrare il file di importazione. Controllare la presenza di eventuali errori."
    IMPORT_FILE_NOT_FOUND, // "Impossibile importare la configurazione, file non trovato!"
    IMPORT_FILE_NOT_VALID, // "Il file di importazione contiene informazioni non valide. Controllare la presenza di eventuali errori."
    DUPLICATE_CATEGORY, // "Impossibile importare la gerarchia %s: categoria duplicata"
    INVALID_CATEGORY, //     "Impossibile importare la gerarchia %s: categoria invalida"
    DUPLICATE_FIELD, //"Impossibile importare la gerarchia %s: campo duplicato"
    DUPLICATE_FIELD_IN_CATEGORY, // "Campo già esistente nella categoria"
    INVALID_FIELD, //("Impossibile importare la gerarchia %s: campo invalido"
    ERR_IMPORTING_HIERARCHY,//"Impossibile importare la gerarchia %s: assicurarsi che non si stiano importando elementi duplicati"
    ERR_IMPORTING_CONFIG, // "Impossibile importare la configurazione!"
    ACTION_NOT_ALLOWED, // "Azione non permessa"
    MISSING_FIELD, // Impossibile creare l'articolo, campo obbligatorio mancante"
    DAY_NOT_VALID, // "Giorno non valido"
    HOUR_NOT_VALID, // "Ora non valida"
    STRING_MUST_NOT_BE_EMPTY, // "La stringa inserita non deve essere vuota"
    INVALID_VALUE, // "Valore non valido"
    DUPLICATE_VALUE, // Valore già inserito
    NO_LEAF_CATEGORY, // "Non ci sono ancora categorie a cui associare un articolo. Contattare un configuratore"
}
