package it.matlice.ingsw.data;

/**
 * rappresenta un tipo di dato, salvandone il tipo e se esso è obbligatorio
 *
 * @param <T>
 */
public record TypeDefinition<T>(it.matlice.ingsw.data.TypeDefinition.TypeAssociation type, boolean required) {
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
