package it.matlice.ingsw.data;

import java.util.Objects;

/**
 * rappresenta un tipo di dato, salvandone il tipo e se esso è obbligatorio
 *
 */
public final class TypeDefinition<T> {
    private final TypeAssociation type;
    private final boolean required;

    public TypeDefinition(boolean required) {
        this.type = TypeAssociation.STRING;
        this.required = required;
    }

    /**
     * @param <T>
     */
    public TypeDefinition(TypeAssociation type, boolean required) {
        this.type = type;
        this.required = required;
    }

    public TypeAssociation type() {
        return type;
    }

    public boolean required() {
        return required;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TypeDefinition) obj;
        return Objects.equals(this.type, that.type) &&
                this.required == that.required;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, required);
    }

    @Override
    public String toString() {
        return "TypeDefinition[" +
                "type=" + type + ", " +
                "required=" + required + ']';
    }

    public enum TypeAssociation {
        STRING(String.class);

        private final Class<?> type;

        TypeAssociation(Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return this.type;
        }
    }
}
