package it.matlice.ingsw.data;

import java.util.HashMap;
import java.util.Set;

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

    public boolean isRequired(String name) {
        return this.containsKey(name) && this.get(name).REQUIRED();
    }

    private Category father = null;

    /**
     * @return ritorna la dimensione della mappa distribuita (size nodo + sum(size(e): e predecessore))
     */
    @Override
    public int size() {
        return father == null ? super.size() : super.size() + father.size();
    }

    /**
     * @return true se la mappa distribuita è vuota (nodo e predecessori)
     */
    @Override
    public boolean isEmpty() {
        return father == null ? super.isEmpty() : super.isEmpty() && father.isEmpty();
    }

    @Override
    public TypeDefinition<?> get(Object key) {
        if (super.containsKey(key) || father == null)
            return super.get(key);
        return father.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (super.containsKey(key) || father == null)
            return super.containsKey(key);
        return father.containsKey(key);
    }

    @Override
    public TypeDefinition<?> put(String key, TypeDefinition<?> value) {
        if (father != null) {
            if (father.containsKey(key))
                father.put(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public TypeDefinition<?> remove(Object key) {
        if (father != null) {
            father.remove(key);
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
        if (father != null)
            set.addAll(father.fullEntrySet());
        return set;
    }

    @Override
    public void clear() {
        if (father != null) father.clear();
        super.clear();
    }


    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value) || (father != null && father.containsValue(value));
    }

    public Category getFather() {
        return father;
    }

    public void setFather(Category father) {
        this.father = father;
    }

    /**
     * identifica se una categoria non ha padre
     *
     * @return true se è una categoria root
     */
    public boolean isRoot() {
        return father == null;
    }

    /**
     * @return ritorna il nome della categoria
     */
    public abstract String getName();
}
