package it.matlice.ingsw.view.stream.datatypes;

import it.matlice.ingsw.model.data.Category;
import it.matlice.ingsw.model.data.Hierarchy;
import it.matlice.ingsw.model.data.Message;
import it.matlice.ingsw.model.data.Offer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DataTypeConverter {

    private final Map<Class<?>, Converter> registeredConverters = new HashMap<>();

    public void registerConverter(Class<?> c, Converter conv){
        this.registeredConverters.put(c, conv);
    }

    public StreamDataType getViewType(Object o){
        for(var converter: this.registeredConverters.entrySet())
            if(converter.getKey().isAssignableFrom(o.getClass()))
                return converter.getValue().convert(o);
        throw new RuntimeException("Invalid data type " + o.getClass().getName());
    }

}
