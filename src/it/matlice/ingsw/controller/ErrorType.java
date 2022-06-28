package it.matlice.ingsw.controller;

public enum ErrorType {
    SYSTEM_NOT_CONFIGURED,
    PASSWORD_NOT_VALID,
    PASSWORD_NOT_SAME,
    USER_NO_AUTH_METHOD,
    USER_DUPLICATE,
    USER_NOT_EXISTING,
    USER_LOGIN_INVALID,
    USER_LOGIN_FAILED,
    ERR_DEFAULT_USER_CREATION,
    ERR_CONFIG_USER_CREATION,
    ERR_OFFER_PROPOSAL,
    CATEGORY_SAME_NAME_ROOT,
    CATEGORY_SAME_NAME,
    COULD_NOT_PARSE_IMPORT_FILE,
    IMPORT_FILE_NOT_FOUND,
    IMPORT_FILE_NOT_VALID,
    DUPLICATE_CATEGORY,
    INVALID_CATEGORY,
    DUPLICATE_FIELD,
    DUPLICATE_FIELD_IN_CATEGORY,
    INVALID_FIELD,
    ERR_IMPORTING_HIERARCHY,
    ERR_IMPORTING_CONFIG,
    ACTION_NOT_ALLOWED,
    MISSING_FIELD,
    DAY_NOT_VALID,
    HOUR_NOT_VALID,
    STRING_MUST_NOT_BE_EMPTY,
    INVALID_VALUE,
    DUPLICATE_VALUE,
    NO_LEAF_CATEGORY,
}