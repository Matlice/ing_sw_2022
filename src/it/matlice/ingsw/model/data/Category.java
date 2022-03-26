package it.matlice.ingsw.model.data;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * La classe rappresenta una categoria.
 * Essendo una hashmap, internamente ad essa avremo le associazioni dei campi nativi.
 * <p>
 * Notare che la struttura dati Category è una mappa distribuita in un albero:
 * - L'aggiunta di campi non ancora definiti avviene sul nodo dove put() viene chiamato
 * - La modifica di campi avviene in uno dei nodi padre (se dispobibile) o nel nodo dove put viene chiamato
 * - size() ritorna la somma dei membri del nodo e quella dei parenti prima di lui
 * - isEmpty() ritorna true se tutti i predecessori e il nodo su cui è chiamato sono vuoti
 * - get() ritorna il membro dal nodo, e se questo non è presente prova a recuperarlo da un antenato
 * - funzionamento analogo per remove() e containsKey() e clear()
 */
public abstract class Category extends HashMap<String, TypeDefinition<?>> {

    private NodeCategory father = null;

    public boolean isRequired(String name) {
        return this.containsKey(name) && this.get(name).required();
    }

    /**
     * @return ritorna la dimensione della mappa distribuita (size nodo + sum(size(e): e predecessore))
     */
    @Override
    public int size() {
        return this.father == null ? super.size() : super.size() + this.father.size();
    }

    /**
     * @return true se la mappa distribuita è vuota (nodo e predecessori)
     */
    @Override
    public boolean isEmpty() {
        return this.father == null ? super.isEmpty() : super.isEmpty() && this.father.isEmpty();
    }

    @Override
    public TypeDefinition<?> get(Object key) {
        if (super.containsKey(key) || this.father == null)
            return super.get(key);
        return this.father.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (super.containsKey(key) || this.father == null)
            return super.containsKey(key);
        return this.father.containsKey(key);
    }

    @Override
    public TypeDefinition<?> put(String key, TypeDefinition<?> value) {
        if (this.father != null) {
            if (this.father.containsKey(key))
                this.father.put(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public TypeDefinition<?> remove(Object key) {
        if (this.father != null) {
            this.father.remove(key);
        }
        return super.remove(key);
    }

    /**
     * Ritorna l'unione degli entry set del nodo e di tutti i suoi antenati
     *
     * @return l'entriset dell'albero fino al nodo corrente
     */
    public Set<Entry<String, TypeDefinition<?>>> fullEntrySet() {
        var set = this.entrySet();
        if (this.father != null)
            set.addAll(this.father.fullEntrySet());
        return set;
    }

    @Override
    public void clear() {
        if (this.father != null) this.father.clear();
        super.clear();
    }


    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value) || (this.father != null && this.father.containsValue(value));
    }

    public boolean isCategoryValid() {
        if (this instanceof LeafCategory)
            return true;
        assert this instanceof NodeCategory;
        return ((NodeCategory) this).getChildren().length >= 2 && Arrays.stream(((NodeCategory) this).getChildren()).allMatch(Category::isCategoryValid);
    }

    public NodeCategory getFather() {
        return this.father;
    }

    public void setFather(NodeCategory father) {
        this.father = father;
    }

    /**
     * identifica se una categoria non ha padre
     *
     * @return true se è una categoria root
     */
    public boolean isRoot() {
        return this.father == null;
    }

    /**
     * @return ritorna il nome della categoria
     */
    public abstract String getName();

    /**
     * @return ritorna la descrizione della categoria
     */
    public abstract String getDescription();

    /**
     * Formatta la categoria in una stringa secondo una struttura ad albero
     * @return stringa rappresentante la categoria
     */
    public String toString(){
        var sb = new StringBuilder();
        this.categoryToString(sb, 0, "");
        return sb.toString();
    }

    /**
     * Passo ricorsivo della formattazione della categoria a stringa,
     * genera la riga per il livello corrente e richiama se stesso sul livello inferiore
     * @param sb StringBuilder su cui scrivere la stringa
     * @param level livelli di indentazione
     * @param prefix prefisso da aggiungere al nome della categoria
     */
    private void categoryToString(@NotNull StringBuilder sb, int level, String prefix){
        sb.append(" ".repeat(level*4));
        sb.append(prefix);
        sb.append(this.getName());

        if (!this.getDescription().isEmpty()) {
            sb.append(" (");
            sb.append(this.getDescription());
            sb.append(")");
        }

        if (!this.entrySet().isEmpty()) {
            sb.append(" <");
            StringJoiner sj = new StringJoiner(", ");
            this.forEach((key, value) -> sj.add(key + (value.required() ? " [R]" : "")));
            sb.append(sj);
            sb.append(">");
        }
        sb.append("\n");

        if(this instanceof NodeCategory)
            for (Category child : ((NodeCategory) this).getChildren()) {
                child.categoryToString(sb, level + 1, "");
            }
    }

    public abstract boolean isValidChildCategoryName(String name);
}
