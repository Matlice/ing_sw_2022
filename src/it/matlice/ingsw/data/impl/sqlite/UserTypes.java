package it.matlice.ingsw.data.impl.sqlite;

public enum UserTypes {
    CONFIGURATOR("sqlite.configurator");

    UserTypes(String typeRepresentation){
        this.typeRepresentation = typeRepresentation;
    }
    private String typeRepresentation;

    public String getTypeRepresentation() {
        return typeRepresentation;
    }
}
