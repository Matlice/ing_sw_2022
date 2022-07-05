package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.controller.ErrorType;
import it.matlice.ingsw.controller.PromptType;
import it.matlice.ingsw.controller.WarningType;

import java.util.HashMap;

import static it.matlice.ingsw.controller.ErrorType.*;
import static it.matlice.ingsw.controller.WarningType.*;
import static it.matlice.ingsw.controller.PromptType.*;

public class ConversionMap {

    private final HashMap<WarningType, String> warnMap = new HashMap<>();
    private final HashMap<ErrorType, String> errorMap = new HashMap<>();
    private final HashMap<PromptType, String> promptMap = new HashMap<>();

    public ConversionMap() {
        this.warnMap.put(NEED_CHANGE_PASSWORD, "Cambia le credenziali di accesso");
        this.warnMap.put(SUCCESSFUL_REGISTRATION, "Utente registrato con successo.");
        this.warnMap.put(NO_OFFERS_IN_EXCHANGE, "Non hai offerte disponibili allo scambio");
        this.warnMap.put(SUCCESSFUL_OFFER_PROPOSAL, "Proposta di scambio confermata");
        this.warnMap.put(NO_ACCEPTABLE_OFFERS, "Non ci sono proposte di scambio accettabili al momento");
        this.warnMap.put(NO_MESSAGES_TO_REPLY, "Non hai messaggi a cui rispondere");
        this.warnMap.put(SUCCESSFUL_ACCEPT_OFFER, "Scambio accettato con successo");
        this.warnMap.put(NO_RETRACTABLE_OFFER, "Non ci sono offerte ritirabili");
        this.warnMap.put(NO_LEAF_CATEGORIES_TO_SHOW, "Non sono presenti categorie di cui mostrare le offerte");
        this.warnMap.put(NO_HIERARCHIES, "Nessuna gerarchia trovata");
        this.warnMap.put(LOADING_CONFIG, "Importando la configurazione...");
        this.warnMap.put(CITY_NOT_OVERWRITABLE_SUCCESS, "La città di scambio non può essere sovrascritta, le altre configurazioni sono state correttamente importate");
        this.warnMap.put(CITY_NOT_OVERWRITABLE, "Non è possibile modificare la piazza di scambio");
        this.warnMap.put(IMPORTING_HIERARCHIES, "Importando le gerarchie...");
        this.warnMap.put(NO_EMPTY_NAME, "Inserire un nome non vuoto");
        this.warnMap.put(OFFER_CREATED, "L'articolo è stato salvato con successo, è ora disponibile allo scambio");
        this.warnMap.put(NO_OFFER_FOUND, "Non sono state trovate offerte");

        this.errorMap.put(SYSTEM_NOT_CONFIGURED, "Il sistema non è ancora utilizzabile. Contattare un configuratore.");
        this.errorMap.put(PASSWORD_NOT_VALID, "La password inserita non rispetta i requisiti di sicurezza");
        this.errorMap.put(PASSWORD_NOT_SAME, "Le password non corrispondono");
        this.errorMap.put(USER_NO_AUTH_METHOD, "Metodo di login non disponibile.");
        this.errorMap.put(USER_DUPLICATE, "L'utente inserito è già esistente.");
        this.errorMap.put(USER_NOT_EXISTING, "Utente non esistente");
        this.errorMap.put(USER_LOGIN_INVALID, "Il login non è più valido.");
        this.errorMap.put(USER_LOGIN_FAILED, "Credenziali errate");
        this.errorMap.put(ERR_DEFAULT_USER_CREATION, "Errore nella creazione dell'utente di default");
        this.errorMap.put(ERR_CONFIG_USER_CREATION, "Impossibile creare un nuovo configuratore");
        this.errorMap.put(ERR_OFFER_PROPOSAL, "Impossibile proporre lo scambio");
        this.errorMap.put(CATEGORY_SAME_NAME_ROOT, "Categoria radice con lo stesso nome già presente");
        this.errorMap.put(CATEGORY_SAME_NAME, "Categoria già esistente nell'albero della gerarchia");
        this.errorMap.put(COULD_NOT_PARSE_IMPORT_FILE, "Impossibile decifrare il file di importazione. Controllare la presenza di eventuali errori.");
        this.errorMap.put(IMPORT_FILE_NOT_FOUND, "Impossibile importare la configurazione, file non trovato!");
        this.errorMap.put(IMPORT_FILE_NOT_VALID, "Il file di importazione contiene informazioni non valide. Controllare la presenza di eventuali errori.");
        this.errorMap.put(DUPLICATE_CATEGORY, "Impossibile importare una gerarchia: categoria duplicata");
        this.errorMap.put(INVALID_CATEGORY, "Impossibile importare una gerarchia: categoria invalida");
        this.errorMap.put(DUPLICATE_FIELD, "Impossibile importare una gerarchia: campo duplicato");
        this.errorMap.put(DUPLICATE_FIELD_IN_CATEGORY, "Campo già esistente nella categoria");
        this.errorMap.put(INVALID_FIELD, "Impossibile importare una gerarchia: campo invalido");
        this.errorMap.put(ERR_IMPORTING_HIERARCHY, "Impossibile importare una gerarchia: assicurarsi che non si stiano importando elementi duplicati");
        this.errorMap.put(ERR_IMPORTING_CONFIG, "Impossibile importare la configurazione!");
        this.errorMap.put(ACTION_NOT_ALLOWED, "Azione non permessa");
        this.errorMap.put(MISSING_FIELD, "Impossibile creare l'articolo, campo obbligatorio mancante");
        this.errorMap.put(DAY_NOT_VALID, "Giorno non valido");
        this.errorMap.put(HOUR_NOT_VALID, "Ora non valida");
        this.errorMap.put(STRING_MUST_NOT_BE_EMPTY, "La stringa inserita non deve essere vuota");
        this.errorMap.put(INVALID_VALUE, "Valore non valido");
        this.errorMap.put(DUPLICATE_VALUE, "Valore già inserito");
        this.errorMap.put(NO_LEAF_CATEGORY, "Non ci sono ancora categorie a cui associare un articolo. Contattare un configuratore");

        this.promptMap.put(SELECT_OPTION_PROMPT, "Scegliere un'opzione");
        this.promptMap.put(SELECT_OPTION_AFTER_LOGIN_PROMPT, "Benvenuto! Scegli un'opzione");
        this.promptMap.put(SELECT_FATHER_HIERARCHY_PROMPT, "Selezionare la categoria padre");
        this.promptMap.put(ADD_HIERARCHY_PROMPT, "Si vuole aggiungere una nuova categoria?\n(nota: una categoria non può avere una sola sottocategoria)");
        this.promptMap.put(ADD_NATIVE_FIELD_PROMPT, "Si vuole aggiungere un campo nativo?");
        this.promptMap.put(IMPORT_CONFIGURATION_PROMPT, "Si vuole importare la configurazione da file?");
        this.promptMap.put(SELECT_PLACE_PROMPT, "Luogo di scambio");
        this.promptMap.put(SELECT_TYPE_PROMPT, "Seleziona un tipo");
        this.promptMap.put(SELECT_FIELD_OR_SAVE_PROMPT, "Scegliere quale campo si vuole compilare (oppure salva)");
        this.promptMap.put(SELECT_CATEGORY_PROMPT, "A quale categoria appartiene l'articolo da creare?");
        this.promptMap.put(SELECT_CATEGORY_FOR_OFFERS_PROMPT, "Di quale categoria si vogliono visualizzare le offerte aperte?");
        this.promptMap.put(SELECT_OFFER_TO_PROPOSE_PROMPT, "Quale offerta si vuole proporre in scambio?");
        this.promptMap.put(SELECT_OFFER_TO_ACCEPT_PROMPT, "Quale offerta si vuole accettare in scambio?");
        this.promptMap.put(SELECT_MESSAGE_PROMPT, "A quale messaggio si vuol rispondere?");
        this.promptMap.put(SELECT_OFFER_TO_RETRIEVE_PROMPT, "Quale offerta si vuole ritirare?");
        this.promptMap.put(SELECT_HIERARCHY_PROMPT, "Quale gerarchia si vuole visualizzare?");
        this.promptMap.put(EMPTY_PROMPT, "");
    }

    public String convertWarningToString(WarningType warn) {
        String r = this.warnMap.get(warn);
        assert r != null;
        return r;
    }

    public String convertErrorToString(ErrorType err) {
        String r = this.errorMap.get(err);
        assert r != null;
        return r;
    }

    public String convertPromptToString(PromptType prompt) {
        String r = this.promptMap.get(prompt);
        assert r != null;
        return r;
    }

}
