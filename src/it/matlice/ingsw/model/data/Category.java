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
public abstract class Category extends HashMap<String, TypeDefinition> {

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
    public TypeDefinition get(Object key) {
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
    public TypeDefinition put(String key, TypeDefinition value) {
        if (this.father != null) {
            if (this.father.containsKey(key))
                this.father.put(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public TypeDefinition remove(Object key) {
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
    public Set<Entry<String, TypeDefinition>> fullEntrySet() {
        var set = new HashSet<>(this.entrySet());
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
     * @return tutte le categorie foglia discendenti (o se stessa se è foglia)
     */
    public abstract List<LeafCategory> getChildLeafs();

    /**
     * Ritorna una stringa che rappresenta la categoria
     * a partire dalla categoria radice
     * (es. "Libro > Romanzo > Romanzo Giallo")
     * @return stringa
     */
    public String fullToString() {
        return this.fullToString(true);
    }

    String fullToString(boolean addDescription) {
        String r;

        if (addDescription && this.getDescription().length() > 0) {
            r = this.getName() + " (" + this.getDescription() + ")";
        } else {
            r = this.getName();
        }

        if (this.father != null) {
            return this.father.fullToString(false) + " > " + r;
        } else {
            return r;
        }
    }

    public abstract boolean isValidChildCategoryName(String name);

    public abstract NodeCategory convertToNode();

}
