package it.matlice.ingsw.data;

public class TypeDefinition<T> {

    public final String FRIENDLY_NAME;
    public final Class<T> TYPE;

    public TypeDefinition(String FRIENDLY_NAME, Class<T> TYPE, boolean REQUIRED) {
        this.FRIENDLY_NAME = FRIENDLY_NAME;
        this.TYPE = TYPE;
        this.REQUIRED = REQUIRED;
    }

    public final boolean REQUIRED;

}
