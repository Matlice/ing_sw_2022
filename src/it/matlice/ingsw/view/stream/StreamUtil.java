package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Interval;
import it.matlice.ingsw.model.data.Offer;

public class StreamUtil {

    public static String timeToString(Interval.Time t) {
        return t.getHour() + ":" + String.format("%02d", t.getMinute());
    }

    public static String intervalToString(Interval i) {
        return timeToString(i.getStart()) + "-" + timeToString(i.getEnd());
    }

    public static String offerToString(Offer o) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.getName()).append(" (").append(o.getCategory().fullToString()).append(") [").append(o.getStatus().getName()).append(" di ").append(o.getOwner().getUsername()).append("]\n");
        for(var k: o.entrySet()) {
            sb.append("\t").append(k.getKey()).append(" = ").append(k.getValue().toString()).append("\n");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

}
