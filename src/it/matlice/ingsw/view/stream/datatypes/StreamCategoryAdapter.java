package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.NodeCategory;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class StreamCategoryAdapter implements StreamDataType {

    private Category category;
    public StreamCategoryAdapter(Category o) {
        this.category = o;
    }

    /**
     * Passo ricorsivo della formattazione della categoria a stringa,
     * genera la riga per il livello corrente e richiama se stesso sul livello inferiore
     * @param sb StringBuilder su cui scrivere la stringa
     * @param level livelli di indentazione
     * @param prefix prefisso da aggiungere al nome della categoria
     */
    private void categoryToString(Category c, @NotNull StringBuilder sb, int level, String prefix){
        sb.append(" ".repeat(level*4));
        sb.append(prefix);
        sb.append(c.getName());

        if (!c.getDescription().isEmpty()) {
            sb.append(" (");
            sb.append(c.getDescription());
            sb.append(")");
        }

        if (!c.entrySet().isEmpty()) {
            sb.append(" <");
            StringJoiner sj = new StringJoiner(", ");
            c.forEach((key, value) -> sj.add(key + (value.required() ? " [R]" : "")));
            sb.append(sj);
            sb.append(">");
        }

        if(c instanceof NodeCategory) {
            for (Category child : ((NodeCategory) c).getChildren()) {
                sb.append("\n");
                this.categoryToString(child, sb, level + 1, "");
            }
        }
    }

    @Override
    public String getStreamRepresentation() {
        var sb = new StringBuilder();
        this.categoryToString(this.category, sb, 0, "");
        return sb.toString();
    }
}
