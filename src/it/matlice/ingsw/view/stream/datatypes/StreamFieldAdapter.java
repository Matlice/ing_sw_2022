package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.TypeDefinition;

import java.util.Map;

public class StreamFieldAdapter implements StreamDataType {

    private final Map.Entry<String, TypeDefinition> field;
    public StreamFieldAdapter(Map.Entry<String, TypeDefinition> f) {
        this.field = f;
    }

    @Override
    public String getStreamRepresentation() {
        var sb = new StringBuilder();
        sb.append(this.field.getKey());

        if (this.field.getValue().required()) {
            sb.append(" [R]");
        }

        return sb.toString();
    }
}
