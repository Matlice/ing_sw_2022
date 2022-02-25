package it.matlice.ingsw.data;

public enum UserTypes {
    CONFIGURATOR("configurator");

    UserTypes(String typeRepresentation) {
        this.typeRepresentation = typeRepresentation;
    }

    private final String typeRepresentation;

    public String getTypeRepresentation() {
        return typeRepresentation;
    }
}
